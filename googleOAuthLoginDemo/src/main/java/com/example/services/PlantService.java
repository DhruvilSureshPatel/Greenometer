package com.example.services;

import com.example.daos.PlantDAO;
import com.google.inject.Inject;

public class PlantService {

    private PlantDAO plantDAO;

    @Inject
    public PlantService(PlantDAO plantDAO) {
        this.plantDAO = plantDAO;
    }

}
