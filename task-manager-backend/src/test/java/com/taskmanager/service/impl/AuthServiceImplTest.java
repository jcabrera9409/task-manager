package com.taskmanager.service.impl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.taskmanager.dto.AuthResponseDTO;
import com.taskmanager.model.Token;
import com.taskmanager.model.User;
import com.taskmanager.repository.TokenRepository;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.service.IJwtService;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthServiceImpl Unit Tests")
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private IJwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    private User testUser;
    private User existingUser;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");

        existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("Existing User");
        existingUser.setEmail("existing@example.com");
        existingUser.setPassword(BCrypt.hashpw("password123", BCrypt.gensalt()));
        existingUser.setActive(true);
    }

    @Test
    @DisplayName("Should register user successfully with valid data")
    void shouldRegisterUserSuccessfully() {
        // Given
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
        doNothing().when(userRepository).persist(any(User.class));

        // When
        User result = authService.register(testUser);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test User");
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getPassword()).isNotEqualTo("password123"); // Should be hashed
        assertThat(BCrypt.checkpw("password123", result.getPassword())).isTrue();
        
        verify(userRepository).findByEmail("test@example.com");
        verify(userRepository).persist(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void shouldThrowExceptionWhenEmailExists() {
        // Given
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(existingUser));

        // When & Then
        assertThatThrownBy(() -> authService.register(testUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email is already registered: test@example.com");

        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("Should login successfully with valid credentials")
    void shouldLoginSuccessfully() {
        // Given
        String mockToken = "mock-jwt-token-123456";
        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(existingUser));
        when(jwtService.generateToken(existingUser)).thenReturn(mockToken);
        when(tokenRepository.invalidateAllTokensForUser(anyLong())).thenReturn(1);
        doNothing().when(tokenRepository).persist(any(Token.class));

        User loginUser = new User();
        loginUser.setEmail("existing@example.com");
        loginUser.setPassword("password123");

        // When
        AuthResponseDTO result = authService.login(loginUser);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAccessToken()).isEqualTo(mockToken);
        assertThat(result.getMessage()).isEqualTo("Login successful");

        verify(userRepository).findByEmail("existing@example.com");
        verify(jwtService).generateToken(existingUser);
        verify(tokenRepository).invalidateAllTokensForUser(1L);
        verify(tokenRepository).persist(any(Token.class));
    }

    @Test
    @DisplayName("Should throw exception with invalid credentials")
    void shouldThrowExceptionWithInvalidCredentials() {
        // Given
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        User loginUser = new User();
        loginUser.setEmail("nonexistent@example.com");
        loginUser.setPassword("wrongpassword");

        // When & Then
        assertThatThrownBy(() -> authService.login(loginUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Invalid Credentials");

        verify(userRepository).findByEmail("nonexistent@example.com");
    }

    @Test
    @DisplayName("Should throw exception when user is inactive")
    void shouldThrowExceptionWhenUserInactive() {
        // Given
        existingUser.setActive(false);
        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(existingUser));

        User loginUser = new User();
        loginUser.setEmail("existing@example.com");
        loginUser.setPassword("password123");

        // When & Then
        assertThatThrownBy(() -> authService.login(loginUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Invalid Credentials")
                .hasCauseInstanceOf(RuntimeException.class);

        verify(userRepository).findByEmail("existing@example.com");
    }

    @Test
    @DisplayName("Should throw exception with wrong password")
    void shouldThrowExceptionWithWrongPassword() {
        // Given
        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(existingUser));

        User loginUser = new User();
        loginUser.setEmail("existing@example.com");
        loginUser.setPassword("wrongpassword");

        // When & Then
        assertThatThrownBy(() -> authService.login(loginUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Invalid Credentials");

        verify(userRepository).findByEmail("existing@example.com");
    }

    @Test
    @DisplayName("Should logout user successfully")
    void shouldLogoutUserSuccessfully() {
        // Given
        String email = "existing@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        when(tokenRepository.invalidateAllTokensForUser(1L)).thenReturn(1);

        // When
        authService.logout(email);

        // Then
        verify(userRepository).findByEmail(email);
        verify(tokenRepository).invalidateAllTokensForUser(1L);
    }

    @Test
    @DisplayName("Should throw exception when logging out non-existent user")
    void shouldThrowExceptionWhenLoggingOutNonExistentUser() {
        // Given
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> authService.logout(email))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error logging out user")
                .hasCauseInstanceOf(RuntimeException.class);

        verify(userRepository).findByEmail(email);
    }
}
