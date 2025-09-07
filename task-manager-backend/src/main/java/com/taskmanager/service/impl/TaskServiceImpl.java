package com.taskmanager.service.impl;

import java.util.List;
import java.util.Optional;

import org.jboss.logging.Logger;

import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.service.ITaskService;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TaskServiceImpl extends CRUDImpl<Task, Long> implements ITaskService {
    private static final Logger LOG = Logger.getLogger(TaskServiceImpl.class);

    @Inject
    private TaskRepository taskRepository;

    @Inject
    private UserRepository userRepository;

    @Override
    protected PanacheRepository<Task> getRepo() {
        return this.taskRepository;
    }

    @Override 
    @Transactional
    public Task create(Task taskEntity) {
        LOG.infof("Creating a new task");
        Optional<User> userObject = userRepository.findByEmail(taskEntity.getUser().getEmail());
        if (userObject.isEmpty()) {
            throw new IllegalArgumentException("User not found with email: " + taskEntity.getUser().getEmail());
        }
        taskEntity.setUser(userObject.get());
        return super.create(taskEntity);
    }   

    @Override
    @Transactional
    public Task update(Task taskEntity) {
        LOG.infof("Updating task with id: %d", taskEntity.getId());
        Optional<User> userObject = userRepository.findByEmail(taskEntity.getUser().getEmail());
        if (userObject.isEmpty()) {
            throw new IllegalArgumentException("User not found with email: " + taskEntity.getUser().getEmail());
        }

        Optional<Task> existingTask = taskRepository.findByIdOptional(taskEntity.getId());
        if (existingTask.isEmpty()) {
            throw new IllegalArgumentException("Task not found with id: " + taskEntity.getId());
        }

        if (!isUserOwnerOfTask(userObject.get(), existingTask.get())) {
            throw new IllegalArgumentException("User is not the owner of the task");
        }

        taskEntity.setUser(userObject.get());
        taskEntity.setCreatedAt(existingTask.get().getCreatedAt());
        taskEntity.setCompleted(existingTask.get().getCompleted());
        return super.update(taskEntity);
    }

    /**
     * Find all tasks for a given user email
     */
    @Override
    public List<Task> findAllByUser(String userEmail) {
        LOG.infof("Finding all tasks for user: %s", userEmail);
        Optional<User> userObject = userRepository.findByEmail(userEmail);
        if (userObject.isEmpty()) {
            throw new IllegalArgumentException("User not found with email: " + userEmail);
        }
        return taskRepository.findByUserId(userObject.get().getId());
    }

    /**
     * Find task by id and user email
     */
    @Override
    public Task findByIdAndUserEmail(Long id, String userEmail) {
        LOG.infof("Finding task with id: %d for user: %s", id, userEmail);
        Optional<User> userObject = userRepository.findByEmail(userEmail);
        if (userObject.isEmpty()) {
            throw new IllegalArgumentException("User not found with email: " + userEmail);
        }
        Optional<Task> taskObject = super.findById(id);
        if (taskObject.isEmpty()) {
            throw new IllegalArgumentException("Task not found with id: " + id + " for user: " + userEmail);
        }
        if (!isUserOwnerOfTask(userObject.get(), taskObject.get())) {
            throw new IllegalArgumentException("User is not the owner of the task");
        }
        return taskObject.get();
    }

    /**
     * Delete task by id and user email
     */

    @Override
    @Transactional
    public void deleteByIdAndUserEmail(Long id, String userEmail) {
        LOG.infof("Deleting task with id: %d for user: %s", id, userEmail);
        Task task = findByIdAndUserEmail(id, userEmail);
        super.delete(task.getId());
    }

    private boolean isUserOwnerOfTask(User user, Task task) {
        return user.getId().equals(task.getUser().getId());
    }
}