package com.taskmanager.controller;

import org.jboss.logging.Logger;

import com.taskmanager.dto.APIResponseDTO;
import com.taskmanager.dto.AuthResponseDTO;
import com.taskmanager.dto.LoginRequestDTO;
import com.taskmanager.model.User;
import com.taskmanager.service.IAuthService;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/rest/api/v1/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    private static final Logger LOG = Logger.getLogger(AuthController.class);

    @Inject
    private IAuthService authService;

    /**
     * Endpoint for registration
     */
    @POST
    @Path("/register")
    @PermitAll
    public Response register(@Valid User user) {
        LOG.infof("Request for registration: %s", user.getEmail());

        try {
            User response = authService.register(user);
            APIResponseDTO<User> responseDTO = APIResponseDTO.success("User registered successfully", response, Response.Status.CREATED.getStatusCode());
            return Response.status(Response.Status.CREATED)
                    .entity(responseDTO)
                    .build();
        } catch (RuntimeException e) {
            LOG.errorf(e, "Error in registration for: %s", user.getEmail());
            APIResponseDTO<String> responseDTO = APIResponseDTO.error(e.getMessage(), Response.Status.BAD_REQUEST.getStatusCode());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(responseDTO)
                    .build();
        } catch (Exception e) {
            LOG.errorf(e, "Internal server error for: %s", user.getEmail());
            APIResponseDTO<String> responseDTO = APIResponseDTO.error("Internal server error", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseDTO)
                    .build();
        }
    }

    /**
     * Endpoint for login
     */
    @POST
    @Path("/login")
    @PermitAll
    public Response login(@Valid LoginRequestDTO request) {
        LOG.infof("Login request for: %s", request.getEmail());

        try {
            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            AuthResponseDTO response = authService.login(user);
            APIResponseDTO<AuthResponseDTO> responseDTO = APIResponseDTO.success("Login successful", response, Response.Status.OK.getStatusCode());
            return Response.ok(responseDTO).build();
        } catch (RuntimeException e) {
            LOG.errorf(e, "Error in login for: %s", request.getEmail());
            APIResponseDTO<String> responseDTO = APIResponseDTO.error(e.getMessage(), Response.Status.UNAUTHORIZED.getStatusCode());
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(responseDTO)
                    .build();
        } catch (Exception e) {
            LOG.errorf(e, "Internal server error in login for: %s", request.getEmail());
            APIResponseDTO<String> responseDTO = APIResponseDTO.error("Internal server error", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseDTO)
                    .build();
        }
    }

    /**
     * Endpoint for logout
     */
    @GET
    @Path("/logout")
    @RolesAllowed("user")
    public Response logout(@Context SecurityContext securityContext) {
        LOG.infof("Logout request for: %s", securityContext.getUserPrincipal().getName());
        try {
            String userEmail = securityContext.getUserPrincipal().getName();
            authService.logout(userEmail);
            APIResponseDTO<String> responseDTO = APIResponseDTO.success("Logout successful", null, Response.Status.OK.getStatusCode());
            return Response.ok(responseDTO).build();
        } catch (RuntimeException e) {
            LOG.errorf(e, "Error in logout for: %s", securityContext.getUserPrincipal().getName());
            APIResponseDTO<String> responseDTO = APIResponseDTO.error(e.getMessage(), Response.Status.BAD_REQUEST.getStatusCode());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(responseDTO)
                    .build();
        } catch (Exception e) {
            LOG.errorf(e, "Internal server error in logout for: %s", securityContext.getUserPrincipal().getName());
            APIResponseDTO<String> responseDTO = APIResponseDTO.error("Internal server error", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseDTO)
                    .build();
        }
    }
}