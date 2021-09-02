package com.example.resources;

import com.example.daos.TestDAO;
import com.example.models.TestModel;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/test")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TestResource {

    TestDAO testDAO;

    @Inject
    public TestResource(TestDAO testDAO) {
        this.testDAO = testDAO;
    }

    @GET
    public Response get() {
        return Response.ok(ImmutableMap.of("message", "hello"), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/create")
    public Response add(TestModel testModel) {
        Optional<Long> id = testDAO.create(testModel);
        Response.ResponseBuilder builder = Response.ok(ImmutableMap.of("id", id.orElse(0L)), MediaType.APPLICATION_JSON);
        return builder.build();
    }

}
