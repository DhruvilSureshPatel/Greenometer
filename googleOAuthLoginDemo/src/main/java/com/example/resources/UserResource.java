package com.example.resources;

import com.example.core.exceptions.CustomException;
import com.example.daos.UserLogin;
import com.example.models.User;
import com.example.services.UserService;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import io.dropwizard.auth.Auth;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private UserService userService;

    @Inject
    public UserResource(UserService userService) {
        this.userService = userService;
    }
    
    @POST
    @Path("/sign-up")
    public Response createUser(@Valid User user) {
        try {
            userService.createNewUser(user);
        } catch (CustomException e) {
            return Response.ok(ImmutableMap.of("status", "false", "message", e.getMessage())).build();
        }
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/sign-in")
    public Response signin(@Valid UserLogin userLogin) {
        String token = userService.isValidUser(userLogin);
        if (token == null) {
            return Response.ok(ImmutableMap.of("status", "false", "message", "Invalid User")).build();
        }
        return Response.ok(ImmutableMap.of("token", token)).build();
    }

    @DELETE
    public Response deleteUserByEmail(@Auth User user) {
        Optional<User> userAuthenticated = userService.authenticateUser(user);
        if (!userAuthenticated.isPresent()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        userService.deleteUser(userAuthenticated.get());
        return Response.ok().build();
    }
    
}
