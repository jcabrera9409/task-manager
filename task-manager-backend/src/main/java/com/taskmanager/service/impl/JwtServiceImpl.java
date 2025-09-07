package com.taskmanager.service.impl;

import java.time.Duration;
import java.util.Set;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import com.taskmanager.model.User;
import com.taskmanager.service.IJwtService;

import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtSignatureException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JwtServiceImpl implements IJwtService {

    private static final Logger LOG = Logger.getLogger(JwtServiceImpl.class);

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    @ConfigProperty(name = "jwt.expiration.time")
    long expirationTime;

    @Override
    public String generateToken(User user) {
        try {
            LOG.infof("Generating token for user: %s", user.getEmail());

            return Jwt.issuer(issuer)
                    .upn(user.getEmail())  
                    .subject(user.getEmail())
                    .claim("name", user.getName())
                    .groups(Set.of("user"))
                    .expiresIn(Duration.ofSeconds(expirationTime))
                    .sign();
                    
        } catch (JwtSignatureException e) {
            LOG.errorf(e, "Error generating JWT token for user: %s", user.getEmail());
            throw new RuntimeException("Error generating JWT token", e);
        }
    }
}