package com.taskmanager.utils;

import java.time.Instant;

import io.smallrye.jwt.auth.principal.JWTCallerPrincipal;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class JwtUtils {

    @Inject
    JWTParser jwtParser; // Quarkus inyecta un parser configurado con tu publicKey

    /**
     * Parsea un token y devuelve el principal
     */
    private JWTCallerPrincipal parseToken(String token) {
        try {
            return (JWTCallerPrincipal) jwtParser.parse(token);
        } catch (ParseException e) {
            throw new RuntimeException("Token inválido", e);
        }
    }

    /** 
     * Extraer correo (subject / upn)
     */
    public String getEmail(String token) {
        JWTCallerPrincipal principal = parseToken(token);
        return principal.getClaim("upn"); // o principal.getSubject()
    }

    /**
     * Extraer nombre
     */
    public String getName(String token) {
        JWTCallerPrincipal principal = parseToken(token);
        return principal.getClaim("name");
    }

    /**
     * Extraer issuer
     */
    public String getIssuer(String token) {
        JWTCallerPrincipal principal = parseToken(token);
        return principal.getIssuer();
    }

    /**
     * Extraer fecha de expiración
     */
    public Instant getExpiration(String token) {
        JWTCallerPrincipal principal = parseToken(token);
        Object exp = principal.getClaim("exp");
        if (exp instanceof Long aLong) {
            return Instant.ofEpochSecond(aLong);
        }
        return null;
    }

    /**
     * Validar si el token expiró
     */
    public boolean isExpired(String token) {
        Instant exp = getExpiration(token);
        return exp != null && Instant.now().isAfter(exp);
    }

    /**
     * Validar issuer
     */
    public boolean isIssuerValid(String token, String expectedIssuer) {
        String iss = getIssuer(token);
        return expectedIssuer.equals(iss);
    }

    /**
     * Validar correo (subject)
     */
    public boolean isEmailValid(String token, String expectedEmail) {
        String email = getEmail(token);
        return expectedEmail.equals(email);
    }
}
