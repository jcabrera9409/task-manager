package com.taskmanager.service;

import java.util.List;

import com.taskmanager.model.Task;

public interface ITaskService extends ICRUD<Task, Long> {

    /**
     * Find all tasks for a given user email
     */
    List<Task> findAllByUser(String userEmail);

    /**
     * Find task by id and user email
     */
    Task findByIdAndUserEmail(Long id, String userEmail);

    /**
     * Delete task by id and user email
     */
    void deleteByIdAndUserEmail(Long id, String userEmail);
}