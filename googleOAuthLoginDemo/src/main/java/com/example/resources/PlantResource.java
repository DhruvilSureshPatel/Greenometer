package com.example.resources;

import com.example.services.PlantService;
import com.google.inject.Inject;

public class PlantResource {

    private PlantService plantService;

    @Inject
    public PlantResource(PlantService plantService) {
        this.plantService = plantService;
    }

}
