package com.taskmanager.repository;

import java.util.Optional;

import com.taskmanager.model.User;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    /**
     * Find a user by their email.
     */
    public Optional<User> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    /**
     * Update password by user ID.
     */
    @Transactional
    public int updatePasswordById(Long id, String password) {
        return update("password = :password where id = :id", 
                      Parameters.with("password", password).and("id", id));
    }
}