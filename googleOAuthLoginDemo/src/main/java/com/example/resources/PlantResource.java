package com.example.resources;

import com.example.core.exceptions.CustomException;
import com.example.core.utils.RestUtils;
import com.example.models.Plant;
import com.example.models.User;
import com.example.services.PlantService;
import com.example.services.UserService;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/plant")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PlantResource {

    private PlantService plantService;
    private UserService userService;

    @Inject
    public PlantResource(PlantService plantService, UserService userService) {
        this.plantService = plantService;
        this.userService = userService;
    }

    @POST
    @Path("/upsert-details")
    public Response addPlant(@Auth User user, Plant plant) throws Exception {
        Optional<User> userAuthenticated = userService.authenticateUser(user);
        if (!userAuthenticated.isPresent()) {
            return RestUtils.getResponseBuilder(Response.Status.UNAUTHORIZED).build();
        }
        plant.setUserId(user.getId());

        plantService.upsertPlantDetails(plant);
        return RestUtils.getResponseBuilder(Response.Status.CREATED).build();
    }

    @GET
    @Path("/{id}")
    public Response getPlant(@Auth User user, @PathParam("id") Long plantId) {
        Optional<User> userAuthenticated = userService.authenticateUser(user);
        if (!userAuthenticated.isPresent()) {
            return RestUtils.getResponseBuilder(Response.Status.UNAUTHORIZED).build();
        }
        Plant plant = plantService.getPlantById(plantId);
        if (plant == null) {
            return RestUtils.getResponseBuilder(Response.Status.OK)
                    .entity(ImmutableMap.of("status", "false", "message", String.format("Plant with Id:%s not found", plantId)))
                    .build();
        }
        return RestUtils.getResponseBuilder(Response.Status.OK).entity(plant).build();
    }

    @GET
    @Path("/get-all")
    public Response getAllPlant(@Auth User user) {
        Optional<User> userAuthenticated = userService.authenticateUser(user);
        if (!userAuthenticated.isPresent()) {
            return RestUtils.getResponseBuilder(Response.Status.UNAUTHORIZED).build();
        }

        List<Plant> plantList = plantService.getAllPlants(user.getId());
        return RestUtils.getResponseBuilder(Response.Status.OK)
                .entity(ImmutableMap.of("status", "true", "count", plantList.size(), "plants", plantList))
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletePlant(@Auth User user, Long plantId) {
        Optional<User> userAuthenticated = userService.authenticateUser(user);
        if (!userAuthenticated.isPresent()) {
            return RestUtils.getResponseBuilder(Response.Status.UNAUTHORIZED).build();
        }

        try {
            plantService.deletePlant(plantId);
        } catch (CustomException e) {
            return RestUtils.getResponseBuilder(Response.Status.OK)
                    .entity(ImmutableMap.of("status", "false", "message", e.getMessage()))
                    .build();
        }

        return RestUtils.getResponseBuilder(Response.Status.OK).build();
    }

}
