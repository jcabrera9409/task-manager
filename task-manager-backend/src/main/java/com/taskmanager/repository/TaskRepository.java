package com.taskmanager.repository;

import java.util.List;

import com.taskmanager.model.Task;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TaskRepository implements PanacheRepository<Task> {
    /**
     * Retrieve all tasks by user id
     */

    public List<Task> findByUserId(Long userId) {
        return list("user.id", userId);
    }
}