package com.taskmanager.configuration;

import java.io.IOException;
import java.util.Optional;

import org.jboss.logging.Logger;

import com.taskmanager.model.Token;
import com.taskmanager.repository.TokenRepository;
import com.taskmanager.utils.JwtUtils;

import io.quarkus.security.UnauthorizedException;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
@ApplicationScoped
public class JwtAuthFilter implements ContainerRequestFilter {
    private static final Logger LOG = Logger.getLogger(JwtAuthFilter.class);

    @Inject
    private JwtUtils jwtUtils;

    @Inject
    private TokenRepository tokenRepository;

    @Inject
    private SecurityIdentity securityIdentity;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        
        if (securityIdentity.isAnonymous()) {
            LOG.info("Request is anonymous, skipping JWT authentication filter");
            return;
        }

        LOG.info("Starting JWT authentication filter");

        String authHeader = requestContext.getHeaderString("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            LOG.warn("Missing or invalid Authorization header");
            throw new UnauthorizedException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring("Bearer ".length());
        LOG.info("Extracted token: " + token);

        try {
            Optional<Token> tokenEntity = tokenRepository.findByAccessToken(token);

            if (tokenEntity.isEmpty() || !tokenEntity.get().isValid()) {
                LOG.warn("Invalid or expired token");
                throw new UnauthorizedException("Invalid or expired token");
            }

            if (jwtUtils.isExpired(token)) {
                LOG.warn("Token has expired");
                throw new UnauthorizedException("Token has expired");
            }
        } catch (UnauthorizedException e) {
            LOG.error("Unauthorized access", e);
            throw new UnauthorizedException("Invalid token", e);
        }
    }
}