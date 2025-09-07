package com.taskmanager.controller;

import org.jboss.logging.Logger;

import com.taskmanager.dto.APIResponseDTO;
import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import com.taskmanager.service.ITaskService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/rest/api/v1/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskController {
    private static final Logger LOG = Logger.getLogger(TaskController.class);

    @Inject
    private ITaskService taskService;

    /**
     * Endpoint for create task
     */
    @POST
    @RolesAllowed("user")
    public Response createTask(@Context SecurityContext securityContext, @Valid Task task) {
        try {
            String userEmail = securityContext.getUserPrincipal().getName();
            LOG.infof("Request to create a new task for user: %s", userEmail);
            User user = new User();
            user.setEmail(userEmail);
            task.setUser(user);
            Task createdTask = taskService.create(task);
            APIResponseDTO<Task> responseDTO = APIResponseDTO.success("Task created successfully", createdTask, Response.Status.CREATED.getStatusCode());
            return Response.status(Response.Status.CREATED)
                    .entity(responseDTO)
                    .build();
        } catch (Exception e) {
            LOG.errorf(e, "Error creating task");
            APIResponseDTO<String> responseDTO = APIResponseDTO.error("Internal server error", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseDTO)
                    .build();
        }
    }

    @PUT
    @RolesAllowed("user")
    public Response updateTask(@Context SecurityContext securityContext, @Valid Task task) {
        try {
            String userEmail = securityContext.getUserPrincipal().getName();
            LOG.infof("Request to update task with id: %d for user: %s", task.getId(), userEmail);
            User user = new User();
            user.setEmail(userEmail);
            task.setUser(user);
            Task updatedTask = taskService.update(task);
            APIResponseDTO<Task> responseDTO = APIResponseDTO.success("Task updated successfully", updatedTask, Response.Status.OK.getStatusCode());
            return Response.status(Response.Status.OK)
                    .entity(responseDTO)
                    .build();
        } catch (Exception e) {
            LOG.errorf(e, "Error updating task");
            APIResponseDTO<String> responseDTO = APIResponseDTO.error("Internal server error", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseDTO)
                    .build();
        }
    
    }
}