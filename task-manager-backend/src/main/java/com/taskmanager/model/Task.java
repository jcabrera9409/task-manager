package com.taskmanager.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tbl_task")
public class Task extends PanacheEntityBase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotBlank(message = "The title is mandatory")
    @Size(min = 2, max = 100, message = "The title must be between 2 and 200 characters")
    @Column(nullable = false, name = "title", length = 200)
    public String title;

    @NotBlank(message = "The description is mandatory")
    @Size(min = 2, max = 1000, message = "The description must be between 2 and 1000 characters")
    @Column(nullable = false, name = "description", length = 1000)
    public String description;

    @JsonIgnore
    @Column(nullable = false, name = "created_at")
    public LocalDateTime createdAt;

    @JsonIgnore
    @Column(nullable = true, name = "updated_at")
    public LocalDateTime updatedAt;

    @Column(nullable = false, name = "completed")
    public Boolean completed;

    @ManyToOne(optional = false)
    public User user;

    // Constructores
    public Task() {
        this.completed = false;
    }

    public Task(String title, String description, User user) {
        this.title = title;
        this.description = description;
        this.completed = false;
        this.user = user;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public Boolean getCompleted() {
        return this.completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Task{ " +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", completed=" + completed +
                ", user=" + (user != null ? user.getEmail() : "null") +
                "}";
    }
}