package com.taskmanager.repository;

import java.util.Optional;

import com.taskmanager.model.Token;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TokenRepository implements PanacheRepository<Token> {

    /**
     * Find a token by its access token string.
     */
    public Optional<Token> findByAccessToken(String accessToken) {
        return find("accessToken", accessToken).firstResultOptional();
    }

    /**
     * Invalidate all tokens for a specific user by setting their loggedOut status to true.
     */
    @Transactional
    public int invalidateAllTokensForUser(Long userId) {
        return update("loggedOut = true where user.id = :userId and loggedOut = false",
                      Parameters.with("userId", userId));
    }
}