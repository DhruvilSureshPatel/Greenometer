package com.example.resources;

import com.example.core.exceptions.CustomException;
import com.example.core.utils.RestUtils;
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
            return RestUtils.getResponseBuilder(Response.Status.OK)
                    .entity(ImmutableMap.of("status", "false", "message", e.getMessage()))
                    .build();
        }
        return RestUtils.getResponseBuilder(Response.Status.CREATED).build();
    }

    @POST
    @Path("/sign-in")
    public Response signin(@Valid UserLogin userLogin) {
        String token = userService.isValidUser(userLogin);
        if (token == null) {
            return RestUtils.getResponseBuilder(Response.Status.OK)
                    .entity(ImmutableMap.of("status", "false", "message","Invalid User"))
                    .build();
        }
        return RestUtils.getResponseBuilder(Response.Status.CREATED).entity(ImmutableMap.of("token", token)).build();
    }

    @DELETE
    public Response deleteUserByEmail(@Auth User user) {
        Optional<User> userAuthenticated = userService.authenticateUser(user);
        if (!userAuthenticated.isPresent()) {
            return RestUtils.getResponseBuilder(Response.Status.UNAUTHORIZED).build();
        }
        userService.deleteUser(userAuthenticated.get());
        return RestUtils.getResponseBuilder(Response.Status.OK).build();
    }

    @POST
    @Path("/add-points/{rewardPoints}")
    public Response addRewardPoints(@Auth User user, @PathParam("rewardPoints") long rewardPoints) throws CustomException {
        Optional<User> userAuthenticated = userService.authenticateUser(user);
        if (!userAuthenticated.isPresent()) {
            return RestUtils.getResponseBuilder(Response.Status.UNAUTHORIZED).build();
        }
        userService.updateRewardPoints(user, rewardPoints, true);
        return RestUtils.getResponseBuilder(Response.Status.OK)
                .entity(ImmutableMap.of("status", "true", "message", "successfully updated reward points"))
                .build();
    }

    @POST
    @Path("/deduct-points/{rewardPoints}")
    public Response decuctRewardPoints(@Auth User user, @PathParam("rewardPoints") long rewardPoints) {
        Optional<User> userAuthenticated = userService.authenticateUser(user);
        if (!userAuthenticated.isPresent()) {
            return RestUtils.getResponseBuilder(Response.Status.UNAUTHORIZED).build();
        }
        try {
            userService.updateRewardPoints(user, rewardPoints, false);
        } catch (CustomException e) {
            return RestUtils.getResponseBuilder(Response.Status.OK)
                    .entity(ImmutableMap.of("status", "false", "message", e.getMessage()))
                    .build();
        }
        return RestUtils.getResponseBuilder(Response.Status.OK)
                .entity(ImmutableMap.of("status", "true", "message", "successfully updated reward points"))
                .build();
    }
    
}
