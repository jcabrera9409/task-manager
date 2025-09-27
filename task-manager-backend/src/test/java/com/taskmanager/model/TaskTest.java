package com.taskmanager.model;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Task Entity Unit Tests")
class TaskTest {

    private Task task;
    private User testUser;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");

        task = new Task();
    }

    @Test
    @DisplayName("Should create task with default constructor")
    void shouldCreateTaskWithDefaultConstructor() {
        // Given & When
        Task newTask = new Task();

        // Then
        assertThat(newTask).isNotNull();
        assertThat(newTask.getCompleted()).isFalse(); // Default value should be false
        assertThat(newTask.getId()).isNull();
        assertThat(newTask.getTitle()).isNull();
        assertThat(newTask.getDescription()).isNull();
        assertThat(newTask.getUser()).isNull();
        assertThat(newTask.getCreatedAt()).isNull();
        assertThat(newTask.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Should create task with constructor parameters")
    void shouldCreateTaskWithConstructorParameters() {
        // Given & When
        Task newTask = new Task("Test Task", "Test Description", testUser);

        // Then
        assertThat(newTask).isNotNull();
        assertThat(newTask.getTitle()).isEqualTo("Test Task");
        assertThat(newTask.getDescription()).isEqualTo("Test Description");
        assertThat(newTask.getUser()).isEqualTo(testUser);
        assertThat(newTask.getCompleted()).isFalse(); // Default value should be false
    }

    @Test
    @DisplayName("Should set and get task properties correctly")
    void shouldSetAndGetTaskPropertiesCorrectly() {
        // Given
        LocalDateTime now = LocalDateTime.now();

        // When
        task.setId(1L);
        task.setTitle("Updated Task");
        task.setDescription("Updated Description");
        task.setCompleted(true);
        task.setUser(testUser);
        task.setCreatedAt(now);

        // Then
        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo("Updated Task");
        assertThat(task.getDescription()).isEqualTo("Updated Description");
        assertThat(task.getCompleted()).isTrue();
        assertThat(task.getUser()).isEqualTo(testUser);
        assertThat(task.getCreatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void shouldHandleNullValuesCorrectly() {
        // When
        task.setTitle(null);
        task.setDescription(null);
        task.setUser(null);
        task.setCreatedAt(null);

        // Then
        assertThat(task.getTitle()).isNull();
        assertThat(task.getDescription()).isNull();
        assertThat(task.getUser()).isNull();
        assertThat(task.getCreatedAt()).isNull();
        assertThat(task.getUpdatedAt()).isNull(); // Default is null
    }

    @Test
    @DisplayName("Should return correct toString representation")
    void shouldReturnCorrectToStringRepresentation() {
        // Given
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setCompleted(false);

        // When
        String result = task.toString();

        // Then
        assertThat(result).contains("Task{");
        assertThat(result).contains("id=1");
        assertThat(result).contains("title='Test Task'");
        assertThat(result).contains("description='Test Description'");
        assertThat(result).contains("completed=false");
    }

    @Test
    @DisplayName("Should test prePersist callback")
    void shouldTestPrePersistCallback() {
        // Given
        task.setTitle("New Task");
        task.setDescription("New Description");
        assertThat(task.getCreatedAt()).isNull();

        // When
        task.prePersist();

        // Then
        assertThat(task.getCreatedAt()).isNotNull();
        assertThat(task.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should test preUpdate callback")
    void shouldTestPreUpdateCallback() {
        // Given
        LocalDateTime originalCreatedAt = LocalDateTime.now().minusDays(1);
        task.setCreatedAt(originalCreatedAt);
        assertThat(task.getUpdatedAt()).isNull(); // Initially null

        // When
        task.preUpdate();

        // Then
        assertThat(task.getCreatedAt()).isEqualTo(originalCreatedAt); // Should not change
        assertThat(task.getUpdatedAt()).isNotNull(); // Should be updated
        assertThat(task.getUpdatedAt()).isAfter(task.getCreatedAt());
    }

    @Test
    @DisplayName("Should handle completed flag toggle")
    void shouldHandleCompletedFlagToggle() {
        // Given
        assertThat(task.getCompleted()).isFalse();

        // When - Mark as completed
        task.setCompleted(true);

        // Then
        assertThat(task.getCompleted()).isTrue();

        // When - Mark as not completed
        task.setCompleted(false);

        // Then
        assertThat(task.getCompleted()).isFalse();
    }
}