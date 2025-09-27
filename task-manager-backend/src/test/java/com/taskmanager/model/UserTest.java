package com.taskmanager.model;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Entity Unit Tests")
class UserTest {

    private User user;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        user = new User();
    }

    @Test
    @DisplayName("Should create user with default constructor")
    void shouldCreateUserWithDefaultConstructor() {
        // Given & When
        User newUser = new User();

        // Then
        assertThat(newUser).isNotNull();
        assertThat(newUser.getActive()).isTrue(); // Default value should be true
        assertThat(newUser.getId()).isNull();
        assertThat(newUser.getName()).isNull();
        assertThat(newUser.getEmail()).isNull();
        assertThat(newUser.getPassword()).isNull();
        assertThat(newUser.getLastUpdated()).isNull();
        assertThat(newUser.getTokens()).isNull();
        assertThat(newUser.getTasks()).isNull();
    }

    @Test
    @DisplayName("Should create user with constructor parameters")
    void shouldCreateUserWithConstructorParameters() {
        // Given & When
        User newUser = new User("John Doe", "john@example.com", "password123");

        // Then
        assertThat(newUser).isNotNull();
        assertThat(newUser.getName()).isEqualTo("John Doe");
        assertThat(newUser.getEmail()).isEqualTo("john@example.com");
        assertThat(newUser.getPassword()).isEqualTo("password123");
        assertThat(newUser.getActive()).isTrue(); // Default value should be true
    }

    @Test
    @DisplayName("Should set and get user properties correctly")
    void shouldSetAndGetUserPropertiesCorrectly() {
        // Given
        LocalDateTime now = LocalDateTime.now();

        // When
        user.setId(1L);
        user.setName("Jane Doe");
        user.setEmail("jane@example.com");
        user.setPassword("securePassword");
        user.setActive(false);
        user.setLastUpdated(now);

        // Then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("Jane Doe");
        assertThat(user.getEmail()).isEqualTo("jane@example.com");
        assertThat(user.getPassword()).isEqualTo("securePassword");
        assertThat(user.getActive()).isFalse();
        assertThat(user.getLastUpdated()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void shouldHandleNullValuesCorrectly() {
        // When
        user.setName(null);
        user.setEmail(null);
        user.setPassword(null);
        user.setLastUpdated(null);
        user.setTokens(null);
        user.setTasks(null);

        // Then
        assertThat(user.getName()).isNull();
        assertThat(user.getEmail()).isNull();
        assertThat(user.getPassword()).isNull();
        assertThat(user.getLastUpdated()).isNull();
        assertThat(user.getTokens()).isNull();
        assertThat(user.getTasks()).isNull();
        assertThat(user.getActive()).isTrue(); // Should maintain default value
    }

    @Test
    @DisplayName("Should return correct toString representation")
    void shouldReturnCorrectToStringRepresentation() {
        // Given
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setActive(true);

        // When
        String result = user.toString();

        // Then
        assertThat(result).contains("User{");
        assertThat(result).contains("id=1");
        assertThat(result).contains("name='John Doe'");
        assertThat(result).contains("email='john@example.com'");
        assertThat(result).contains("active=true");
    }

    @Test
    @DisplayName("Should test prePersist callback")
    void shouldTestPrePersistCallback() {
        // Given
        user.setName("New User");
        user.setEmail("newuser@example.com");
        assertThat(user.getLastUpdated()).isNull();

        // When
        user.prePersist();

        // Then
        assertThat(user.getLastUpdated()).isNotNull();
        assertThat(user.getLastUpdated()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should test preUpdate callback")
    void shouldTestPreUpdateCallback() {
        // Given
        LocalDateTime originalTime = LocalDateTime.now().minusHours(1);
        user.setLastUpdated(originalTime);

        // When
        user.preUpdate();

        // Then
        assertThat(user.getLastUpdated()).isAfter(originalTime);
        assertThat(user.getLastUpdated()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should handle active flag toggle")
    void shouldHandleActiveFlagToggle() {
        // Given
        assertThat(user.getActive()).isTrue(); // Default

        // When - Deactivate user
        user.setActive(false);

        // Then
        assertThat(user.getActive()).isFalse();

        // When - Reactivate user
        user.setActive(true);

        // Then
        assertThat(user.getActive()).isTrue();
    }

    @Test
    @DisplayName("Should handle setting user to null active")
    void shouldHandleSettingUserToNullActive() {
        // When
        user.setActive(null);

        // Then
        assertThat(user.getActive()).isNull();
    }
}