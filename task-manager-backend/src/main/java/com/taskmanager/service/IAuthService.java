package com.taskmanager.service;

import com.taskmanager.dto.AuthResponseDTO;
import com.taskmanager.model.User;

public interface IAuthService {

    /**
     * Register a new user
     */
    User register(User user);

    /**
     * Authenticate user and generate token
     */
    AuthResponseDTO login(User user);

    /**
     * Logout user by invalidating their tokens
     */
    void logout(String email);

}