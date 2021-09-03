package com.example.resources;

import com.example.client.PlantIdClient;
import com.example.core.utils.RestUtils;
import com.example.models.Plant;
import com.example.models.User;
import com.example.services.PlantService;
import com.example.services.UserService;
import com.google.inject.Inject;
import io.dropwizard.auth.Auth;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Path("/plant")
public class PlantResource {

    private PlantService plantService;
    private UserService userService;

    @Inject
    public PlantResource(PlantService plantService, UserService userService) {
        this.plantService = plantService;
        this.userService = userService;
    }

    @POST
    @Path("/add")
    public Response addPlant(@Auth User user, Plant plant) throws Exception {
        Optional<User> userAuthenticated = userService.authenticateUser(user);
        if (!userAuthenticated.isPresent()) {
            return RestUtils.getResponseBuilder(Response.Status.UNAUTHORIZED).build();
        }
        plant.setUserId(user.getId());

        Map<String, Object> response = PlantIdClient.getDetails(plant.getImageData());
        if (response.get("suggestions") != null) {
            plant.setType((String) (((Map<String, Object>) ((Map<String, Object>) ((List<Object>) response.get("suggestions")).get(0)).get("plant_details")).get("scientific_name")));
        }

        return null;
    }

}
