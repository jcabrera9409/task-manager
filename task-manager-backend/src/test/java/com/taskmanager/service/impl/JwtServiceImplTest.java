package com.taskmanager.service.impl;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.taskmanager.model.User;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtServiceImpl Unit Tests")
class JwtServiceImplTest {

    @InjectMocks
    private JwtServiceImpl jwtService;

    private User testUser;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setActive(true);
        testUser.setLastUpdated(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should generate JWT token successfully for valid user")
    void shouldGenerateJwtTokenSuccessfully() {
        // When
        String token = jwtService.generateToken(testUser);

        // Then
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token).contains(".");  // JWT tokens contain dots as separators
    }

    @Test
    @DisplayName("Should throw exception when generating token for null user")
    void shouldThrowExceptionWhenGeneratingTokenForNullUser() {
        // When & Then
        assertThatThrownBy(() -> jwtService.generateToken(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should generate different tokens for different users")
    void shouldGenerateDifferentTokensForDifferentUsers() {
        // Given
        User anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setName("Another User");
        anotherUser.setEmail("another@example.com");
        anotherUser.setActive(true);

        // When
        String token1 = jwtService.generateToken(testUser);
        String token2 = jwtService.generateToken(anotherUser);

        // Then
        assertThat(token1).isNotNull();
        assertThat(token2).isNotNull();
        assertThat(token1).isNotEqualTo(token2);
    }

    @Test
    @DisplayName("Should generate token with consistent format")
    void shouldGenerateTokenWithConsistentFormat() {
        // When
        String token = jwtService.generateToken(testUser);

        // Then
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        
        // JWT tokens have 3 parts separated by dots
        String[] tokenParts = token.split("\\.");
        assertThat(tokenParts).hasSize(3);
        
        // Each part should not be empty
        assertThat(tokenParts[0]).isNotEmpty(); // Header
        assertThat(tokenParts[1]).isNotEmpty(); // Payload
        assertThat(tokenParts[2]).isNotEmpty(); // Signature
    }

    @Test
    @DisplayName("Should handle user with special characters in email")
    void shouldHandleUserWithSpecialCharactersInEmail() {
        // Given
        User specialUser = new User();
        specialUser.setId(3L);
        specialUser.setName("Special User");
        specialUser.setEmail("user+test@example-domain.com");
        specialUser.setActive(true);

        // When
        String token = jwtService.generateToken(specialUser);

        // Then
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token).contains(".");
    }

    @Test
    @DisplayName("Should generate token for user with minimal required fields")
    void shouldGenerateTokenForUserWithMinimalRequiredFields() {
        // Given
        User minimalUser = new User();
        minimalUser.setId(3L); // ID is required for JWT generation
        minimalUser.setName("Minimal User"); // Name is required
        minimalUser.setEmail("minimal@example.com");

        // When
        String token = jwtService.generateToken(minimalUser);

        // Then
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    @DisplayName("Should generate token for user with long email")
    void shouldGenerateTokenForUserWithLongEmail() {
        // Given
        User userWithLongEmail = new User();
        userWithLongEmail.setId(4L);
        userWithLongEmail.setName("User with Long Email");
        userWithLongEmail.setEmail("very.long.email.address.for.testing.purposes@example-domain-name.com");

        // When
        String token = jwtService.generateToken(userWithLongEmail);

        // Then
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }
}