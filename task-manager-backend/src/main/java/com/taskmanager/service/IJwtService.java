package com.taskmanager.service;

import com.taskmanager.model.User;

public interface IJwtService {

    /**
     * Generate a JWT token for a given user
     */
    String generateToken(User user);

}