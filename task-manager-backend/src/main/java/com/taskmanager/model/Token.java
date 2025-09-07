package com.taskmanager.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_token")
public class Token extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    
    @Column(nullable = false, unique = true, name = "access_token", length = 2048)
    public String accessToken;
    
    @Column(nullable = false, unique = true, name = "refresh_token", length = 2048)
    public String refreshToken;
    
    @Column(nullable = false, name = "logged_out")
    public boolean loggedOut;
    
    @ManyToOne(optional = false)
    public User user;

    // Bussiness methods
    public boolean isValid() {
        return !this.loggedOut;
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