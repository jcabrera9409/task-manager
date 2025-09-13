package com.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class AuthResponseDTO {
    
    @JsonProperty("access_token")
    private final String accessToken;

    @JsonProperty("message")
    private final String message;

    public AuthResponseDTO(String accessToken, String message) {
        this.accessToken = accessToken;
        this.message = message;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getMessage() {
        return this.message;
    }
}