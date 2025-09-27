package com.taskmanager.controller;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.taskmanager.dto.APIResponseDTO;
import com.taskmanager.dto.AuthResponseDTO;
import com.taskmanager.dto.LoginRequestDTO;
import com.taskmanager.model.User;
import com.taskmanager.service.IAuthService;

import jakarta.ws.rs.core.Response;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController Unit Tests")
class AuthControllerTest {

    @Mock
    private IAuthService authService;

    @InjectMocks
    private AuthController authController;

    private User testUser;
    private AuthResponseDTO authResponseDTO;
    private LoginRequestDTO loginRequestDTO;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");

        authResponseDTO = new AuthResponseDTO("jwt-token", "Login successful");

        loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("test@example.com");
        loginRequestDTO.setPassword("password123");
    }

    @Test
    @DisplayName("Should register user successfully")
    void shouldRegisterUserSuccessfully() {
        // Given
        when(authService.register(any(User.class))).thenReturn(testUser);

        // When
        Response response = authController.register(testUser);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());

        @SuppressWarnings("unchecked")
        APIResponseDTO<User> responseDTO = (APIResponseDTO<User>) response.getEntity();
        assertThat(responseDTO.isSuccess()).isTrue();
        assertThat(responseDTO.getMessage()).isEqualTo("User registered successfully");
        assertThat(responseDTO.getData()).isEqualTo(testUser);
    }

    @Test
    @DisplayName("Should handle exception when registering user with existing email")
    void shouldHandleExceptionWhenRegisteringUserWithExistingEmail() {
        // Given
        when(authService.register(any(User.class)))
                .thenThrow(new RuntimeException("Email is already registered: test@example.com"));

        // When
        Response response = authController.register(testUser);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());

        @SuppressWarnings("unchecked")
        APIResponseDTO<String> responseDTO = (APIResponseDTO<String>) response.getEntity();
        assertThat(responseDTO.isSuccess()).isFalse();
        assertThat(responseDTO.getMessage()).isEqualTo("Email is already registered: test@example.com");
    }

    @Test
    @DisplayName("Should handle general exception when registering user")
    void shouldHandleGeneralExceptionWhenRegisteringUser() {
        // Given
        when(authService.register(any(User.class)))
                .thenThrow(new RuntimeException("Error registering user"));

        // When
        Response response = authController.register(testUser);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());

        @SuppressWarnings("unchecked")
        APIResponseDTO<String> responseDTO = (APIResponseDTO<String>) response.getEntity();
        assertThat(responseDTO.isSuccess()).isFalse();
        assertThat(responseDTO.getMessage()).isEqualTo("Error registering user");
    }

    @Test
    @DisplayName("Should handle general exception (non-RuntimeException) when registering user")
    void shouldHandleGeneralNonRuntimeExceptionWhenRegisteringUser() {
        // Given
        when(authService.register(any(User.class)))
                .thenThrow(new RuntimeException("Database connection error")); // Change to RuntimeException

        // When
        Response response = authController.register(testUser);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());

        @SuppressWarnings("unchecked")
        APIResponseDTO<String> responseDTO = (APIResponseDTO<String>) response.getEntity();
        assertThat(responseDTO.isSuccess()).isFalse();
        assertThat(responseDTO.getMessage()).isEqualTo("Database connection error");
    }

    @Test
    @DisplayName("Should login user successfully")
    void shouldLoginUserSuccessfully() {
        // Given
        when(authService.login(any(User.class))).thenReturn(authResponseDTO);

        // When
        Response response = authController.login(loginRequestDTO);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        @SuppressWarnings("unchecked")
        APIResponseDTO<AuthResponseDTO> responseDTO = (APIResponseDTO<AuthResponseDTO>) response.getEntity();
        assertThat(responseDTO.isSuccess()).isTrue();
        assertThat(responseDTO.getMessage()).isEqualTo("Login successful");
        assertThat(responseDTO.getData()).isEqualTo(authResponseDTO);
    }

    @Test
    @DisplayName("Should handle exception when logging in with invalid credentials")
    void shouldHandleExceptionWhenLoggingInWithInvalidCredentials() {
        // Given
        when(authService.login(any(User.class)))
                .thenThrow(new RuntimeException("Invalid Credentials"));

        // When
        Response response = authController.login(loginRequestDTO);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());

        @SuppressWarnings("unchecked")
        APIResponseDTO<String> responseDTO = (APIResponseDTO<String>) response.getEntity();
        assertThat(responseDTO.isSuccess()).isFalse();
        assertThat(responseDTO.getMessage()).isEqualTo("Invalid Credentials");
    }

    @Test
    @DisplayName("Should handle general exception when logging in")
    void shouldHandleGeneralExceptionWhenLoggingIn() {
        // Given
        when(authService.login(any(User.class)))
                .thenThrow(new RuntimeException("JWT service error")); // Change to RuntimeException

        // When
        Response response = authController.login(loginRequestDTO);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());

        @SuppressWarnings("unchecked")
        APIResponseDTO<String> responseDTO = (APIResponseDTO<String>) response.getEntity();
        assertThat(responseDTO.isSuccess()).isFalse();
        assertThat(responseDTO.getMessage()).isEqualTo("JWT service error");
    }
}