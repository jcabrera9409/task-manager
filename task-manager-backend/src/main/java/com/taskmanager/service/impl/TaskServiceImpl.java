package com.taskmanager.service.impl;

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
        LOG.info("Creating a new task");
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

    private boolean isUserOwnerOfTask(User user, Task task) {
        return user.getId().equals(task.getUser().getId());
    }
}