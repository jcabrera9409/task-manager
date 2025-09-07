package com.taskmanager.service;

import java.util.List;
import java.util.Optional;

public interface ICRUD<T, ID> {

    /**
     * Create a new entity.
     */
    T create(T entity);

    /**
     * Update an existing entity.
     */
    T update(T entity);

    /**
     * Delete an entity by its ID.
     */
    void delete(ID id);

    /**
     * Find an entity by its ID.
     */
    Optional<T> findById(ID id);

    /**
     * Retrieve all entities.
     */
    List<T> findAll();
}