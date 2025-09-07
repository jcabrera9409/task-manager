package com.taskmanager.service.impl;

import org.jboss.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;

import com.taskmanager.dto.AuthResponseDTO;
import com.taskmanager.model.Token;
import com.taskmanager.model.User;
import com.taskmanager.repository.TokenRepository;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.service.IAuthService;
import com.taskmanager.service.IJwtService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AuthServiceImpl implements IAuthService {

    private static final Logger LOG = Logger.getLogger(AuthServiceImpl.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private TokenRepository tokenRepository;

    @Inject
    private IJwtService jwtService;

    @Override
    @Transactional
    public User register(User user) {
        LOG.infof("Initiating registration for user: %s", user.getEmail());

        // Verificar si el email ya existe
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already registered: " + user.getEmail());
        }

        try {
            User newUser = new User();
            newUser.setName(user.getName());
            newUser.setEmail(user.getEmail());
            newUser.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

            // Guardar usuario
            userRepository.persist(newUser);
            LOG.infof("User registered successfully: %s", newUser.getEmail());

            return newUser;

        } catch (Exception e) {
            LOG.errorf(e, "Error registering user: %s", user.getEmail());
            throw new RuntimeException("Error registering user", e);
        }
    }

    @Override
    @Transactional
    public AuthResponseDTO login(User user) {
        LOG.infof("Initiating login for user: %s", user.getEmail());

        try {
            // Buscar usuario por email
            User userExist = userRepository.findByEmail(user.getEmail())
                    .orElseThrow(() -> new RuntimeException("Invalid Credentials"));

            if (!userExist.getActive()) {
                throw new RuntimeException("User is inactive");
            }

            if (!BCrypt.checkpw(user.getPassword(), userExist.getPassword())) {
                throw new RuntimeException("Invalid Credentials");
            }

            LOG.infof("Login successful for user: %s", userExist.getEmail());

            String token = jwtService.generateToken(userExist);
            Token newToken = new Token();
            newToken.setAccessToken(token);
            newToken.setRefreshToken("");
            newToken.setUser(userExist);
            
            tokenRepository.invalidateAllTokensForUser(userExist.getId());
            tokenRepository.persist(newToken);

            return new AuthResponseDTO(token, "Login successful");

        } catch (RuntimeException e) {
            LOG.errorf(e, "Error en login para usuario: %s", user.getEmail());
            throw new RuntimeException("Invalid Credentials", e);
        }
    }

    @Override
    @Transactional
    public void logout(String email) {
        LOG.infof("Logging out user with email: %s", email);
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
            Long userId = user.getId();
            tokenRepository.invalidateAllTokensForUser(userId);
            LOG.infof("User with email %s logged out successfully", email);
        } catch (Exception e) {
            LOG.errorf(e, "Error logging out user with email: %s", email);
            throw new RuntimeException("Error logging out user", e);
        }
    }
}