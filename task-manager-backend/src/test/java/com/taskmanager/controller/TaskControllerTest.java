package com.taskmanager.controller;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.taskmanager.dto.APIResponseDTO;
import com.taskmanager.model.Task;
import com.taskmanager.service.ITaskService;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskController Unit Tests")
class TaskControllerTest {

    @Mock
    private ITaskService taskService;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private TaskController taskController;

    private Task testTask;
    private final String userEmail = "test@example.com";

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("Test Task");
        testTask.setDescription("Test Description");
        testTask.setCompleted(false);

        // Mock SecurityContext
        when(securityContext.getUserPrincipal()).thenReturn(() -> userEmail);
    }

    @Test
    @DisplayName("Should create task successfully")
    void shouldCreateTaskSuccessfully() {
        // Given
        Task newTask = new Task();
        newTask.setTitle("New Task");
        newTask.setDescription("New Description");

        when(taskService.create(any(Task.class))).thenReturn(testTask);

        // When
        Response response = taskController.createTask(securityContext, newTask);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        
        @SuppressWarnings("unchecked")
        APIResponseDTO<Task> responseDTO = (APIResponseDTO<Task>) response.getEntity();
        assertThat(responseDTO.isSuccess()).isTrue();
        assertThat(responseDTO.getMessage()).isEqualTo("Task created successfully");
        assertThat(responseDTO.getData()).isEqualTo(testTask);

        verify(taskService).create(any(Task.class));
    }

    @Test
    @DisplayName("Should handle exception when creating task")
    void shouldHandleExceptionWhenCreatingTask() {
        // Given
        Task newTask = new Task();
        newTask.setTitle("New Task");

        when(taskService.create(any(Task.class))).thenThrow(new IllegalArgumentException("User not found"));

        // When
        Response response = taskController.createTask(securityContext, newTask);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());

        @SuppressWarnings("unchecked")
        APIResponseDTO<String> responseDTO = (APIResponseDTO<String>) response.getEntity();
        assertThat(responseDTO.isSuccess()).isFalse();
        assertThat(responseDTO.getMessage()).isEqualTo("User not found");

        verify(taskService).create(any(Task.class));
    }

    @Test
    @DisplayName("Should get all tasks successfully")
    void shouldGetAllTasksSuccessfully() {
        // Given
        List<Task> tasks = Arrays.asList(testTask);
        when(taskService.findAllByUser(userEmail)).thenReturn(tasks);

        // When
        Response response = taskController.getAllTasks(securityContext);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        @SuppressWarnings("unchecked")
        APIResponseDTO<List<Task>> responseDTO = (APIResponseDTO<List<Task>>) response.getEntity();
        assertThat(responseDTO.isSuccess()).isTrue();
        assertThat(responseDTO.getMessage()).isEqualTo("Tasks retrieved successfully");
        assertThat(responseDTO.getData()).isEqualTo(tasks);

        verify(taskService).findAllByUser(userEmail);
    }

    @Test
    @DisplayName("Should get task by id successfully")
    void shouldGetTaskByIdSuccessfully() {
        // Given
        when(taskService.findByIdAndUserEmail(1L, userEmail)).thenReturn(testTask);

        // When
        Response response = taskController.getTaskById(securityContext, 1L);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        @SuppressWarnings("unchecked")
        APIResponseDTO<Task> responseDTO = (APIResponseDTO<Task>) response.getEntity();
        assertThat(responseDTO.isSuccess()).isTrue();
        assertThat(responseDTO.getMessage()).isEqualTo("Task retrieved successfully");
        assertThat(responseDTO.getData()).isEqualTo(testTask);

        verify(taskService).findByIdAndUserEmail(1L, userEmail);
    }

    @Test
    @DisplayName("Should handle exception when getting task by id")
    void shouldHandleExceptionWhenGettingTaskById() {
        // Given
        when(taskService.findByIdAndUserEmail(1L, userEmail))
                .thenThrow(new IllegalArgumentException("Task not found"));

        // When
        Response response = taskController.getTaskById(securityContext, 1L);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());

        @SuppressWarnings("unchecked")
        APIResponseDTO<String> responseDTO = (APIResponseDTO<String>) response.getEntity();
        assertThat(responseDTO.isSuccess()).isFalse();
        assertThat(responseDTO.getMessage()).isEqualTo("Task not found");

        verify(taskService).findByIdAndUserEmail(1L, userEmail);
    }

    @Test
    @DisplayName("Should update task successfully")
    void shouldUpdateTaskSuccessfully() {
        // Given
        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Updated Task");

        when(taskService.update(any(Task.class))).thenReturn(testTask);

        // When
        Response response = taskController.updateTask(securityContext, updatedTask);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        @SuppressWarnings("unchecked")
        APIResponseDTO<Task> responseDTO = (APIResponseDTO<Task>) response.getEntity();
        assertThat(responseDTO.isSuccess()).isTrue();
        assertThat(responseDTO.getMessage()).isEqualTo("Task updated successfully");
        assertThat(responseDTO.getData()).isEqualTo(testTask);

        verify(taskService).update(any(Task.class));
    }

    @Test
    @DisplayName("Should handle exception when updating task")
    void shouldHandleExceptionWhenUpdatingTask() {
        // Given
        Task updatedTask = new Task();
        updatedTask.setId(1L);

        when(taskService.update(any(Task.class)))
                .thenThrow(new IllegalArgumentException("User is not the owner of the task"));

        // When
        Response response = taskController.updateTask(securityContext, updatedTask);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());

        @SuppressWarnings("unchecked")
        APIResponseDTO<String> responseDTO = (APIResponseDTO<String>) response.getEntity();
        assertThat(responseDTO.isSuccess()).isFalse();
        assertThat(responseDTO.getMessage()).isEqualTo("User is not the owner of the task");

        verify(taskService).update(any(Task.class));
    }

    @Test
    @DisplayName("Should delete task successfully")
    void shouldDeleteTaskSuccessfully() {
        // When
        Response response = taskController.deleteTaskById(securityContext, 1L);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        @SuppressWarnings("unchecked")
        APIResponseDTO<String> responseDTO = (APIResponseDTO<String>) response.getEntity();
        assertThat(responseDTO.isSuccess()).isTrue();
        assertThat(responseDTO.getMessage()).isEqualTo("Task deleted successfully");

        verify(taskService).deleteByIdAndUserEmail(1L, userEmail);
    }

    @Test
    @DisplayName("Should handle exception when deleting task")
    void shouldHandleExceptionWhenDeletingTask() {
        // Given
        doThrow(new IllegalArgumentException("Task not found")).when(taskService)
                .deleteByIdAndUserEmail(1L, userEmail);

        // When
        Response response = taskController.deleteTaskById(securityContext, 1L);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());

        @SuppressWarnings("unchecked")
        APIResponseDTO<String> responseDTO = (APIResponseDTO<String>) response.getEntity();
        assertThat(responseDTO.isSuccess()).isFalse();
        assertThat(responseDTO.getMessage()).isEqualTo("Task not found");

        verify(taskService).deleteByIdAndUserEmail(1L, userEmail);
    }

    @Test
    @DisplayName("Should mark task as completed successfully")
    void shouldMarkTaskAsCompletedSuccessfully() {
        // When
        Response response = taskController.markTaskAsCompleted(securityContext, 1L);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        @SuppressWarnings("unchecked")
        APIResponseDTO<String> responseDTO = (APIResponseDTO<String>) response.getEntity();
        assertThat(responseDTO.isSuccess()).isTrue();
        assertThat(responseDTO.getMessage()).isEqualTo("Task marked as completed successfully");

        verify(taskService).markAsCompleted(1L, userEmail);
    }

    @Test
    @DisplayName("Should handle exception when marking task as completed with already completed task")
    void shouldHandleExceptionWhenMarkingAlreadyCompletedTask() {
        // Given
        doThrow(new IllegalArgumentException("Task is already completed")).when(taskService)
                .markAsCompleted(1L, userEmail);

        // When
        Response response = taskController.markTaskAsCompleted(securityContext, 1L);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());

        @SuppressWarnings("unchecked")
        APIResponseDTO<String> responseDTO = (APIResponseDTO<String>) response.getEntity();
        assertThat(responseDTO.isSuccess()).isFalse();
        assertThat(responseDTO.getMessage()).isEqualTo("Task is already completed");

        verify(taskService).markAsCompleted(1L, userEmail);
    }

    @Test
    @DisplayName("Should handle exception when marking task as completed with non-owner user")
    void shouldHandleExceptionWhenMarkingTaskAsCompletedWithNonOwnerUser() {
        // Given
        doThrow(new IllegalArgumentException("User is not the owner of the task")).when(taskService)
                .markAsCompleted(1L, userEmail);

        // When
        Response response = taskController.markTaskAsCompleted(securityContext, 1L);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());

        @SuppressWarnings("unchecked")
        APIResponseDTO<String> responseDTO = (APIResponseDTO<String>) response.getEntity();
        assertThat(responseDTO.isSuccess()).isFalse();
        assertThat(responseDTO.getMessage()).isEqualTo("User is not the owner of the task");

        verify(taskService).markAsCompleted(1L, userEmail);
    }

    @Test
    @DisplayName("Should handle general exception when marking task as completed")
    void shouldHandleGeneralExceptionWhenMarkingTaskAsCompleted() {
        // Given
        doThrow(new RuntimeException("Database error")).when(taskService)
                .markAsCompleted(1L, userEmail);

        // When
        Response response = taskController.markTaskAsCompleted(securityContext, 1L);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());

        @SuppressWarnings("unchecked")
        APIResponseDTO<String> responseDTO = (APIResponseDTO<String>) response.getEntity();
        assertThat(responseDTO.isSuccess()).isFalse();
        assertThat(responseDTO.getMessage()).isEqualTo("Internal server error");

        verify(taskService).markAsCompleted(1L, userEmail);
    }
}