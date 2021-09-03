package com.example.services;

import com.example.client.GCSClient;
import com.example.daos.PlantDAO;
import com.example.models.Plant;
import com.google.inject.Inject;


public class PlantService {

    private PlantDAO plantDAO;

    @Inject
    public PlantService(PlantDAO plantDAO) {
        this.plantDAO = plantDAO;
    }

    public void addPlant(Plant plant) {

    }

}
