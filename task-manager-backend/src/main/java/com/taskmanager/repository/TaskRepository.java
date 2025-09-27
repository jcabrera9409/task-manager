package com.taskmanager.repository;

import java.util.List;

import com.taskmanager.model.Task;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TaskRepository implements PanacheRepository<Task> {
   
    /**
     * Retrieve all tasks by user id
     */
    public List<Task> findByUserId(Long userId) {
        return list("user.id", userId);
    }

    /**
     * Update completed status of a task
     */
    @Transactional
    public int updateCompletedStatus(Long taskId) {
        return update("completed = true where id = :taskId", 
                        Parameters.with("taskId", taskId));
    }
}