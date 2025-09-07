package com.taskmanager.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_token")
public class Token extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, name = "access_token", length = 2048)
    private String accessToken;

    @Column(nullable = true, unique = true, name = "refresh_token", length = 2048)
    private String refreshToken;

    @Column(nullable = false, name = "logged_out")
    private boolean loggedOut;

    @ManyToOne(optional = false)
    private User user;

    // Getters and Setters
    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Bussiness methods
    public boolean isValid() {
        return !this.loggedOut;
    }

    @PrePersist
    public void prePersist() {
        this.loggedOut = false;
    }
    
    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", loggedOut=" + loggedOut +
                ", usuario=" + (user != null ? user.getEmail() : "null") +
                '}';
    }
}