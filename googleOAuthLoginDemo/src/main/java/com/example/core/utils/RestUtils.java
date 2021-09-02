package com.example.core.utils;

import javax.ws.rs.core.Response;

public class RestUtils {

    public static Response.ResponseBuilder getResponseBuilder(Response.Status status) {
        return Response
                .status(status)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Headers",
                        "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Methods",
                        "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }

}
