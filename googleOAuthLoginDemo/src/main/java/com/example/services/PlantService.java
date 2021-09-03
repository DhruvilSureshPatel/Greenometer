package com.example.services;

import com.example.client.GCSClient;
import com.example.client.PlantIdClient;
import com.example.core.exceptions.CustomException;
import com.example.daos.PlantDAO;
import com.example.models.Plant;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BoundingPoly;
import com.google.cloud.vision.v1.LocalizedObjectAnnotation;
import com.google.cloud.vision.v1.NormalizedVertex;
import com.google.inject.Inject;

import java.util.List;
import java.util.Map;


public class PlantService {

    private PlantDAO plantDAO;

    @Inject
    public PlantService(PlantDAO plantDAO) {
        this.plantDAO = plantDAO;
    }

    public void upsertPlantDetails(Plant plant) throws Exception {
        Plant plantFound = null;
        if (plant.getId() != null) {
            plantDAO.getPlant(plant.getId());
        }

        AnnotateImageResponse annotateImageResponse = GCSClient.processBase64Image(plant.getImageData());
        List<LocalizedObjectAnnotation> localizedObjectAnnotations = annotateImageResponse.getLocalizedObjectAnnotationsList();
        if (localizedObjectAnnotations.isEmpty()) {
            throw new CustomException("Please retry capturing the image");
        }
        for(LocalizedObjectAnnotation localizedObjectAnnotation: localizedObjectAnnotations) {
            String name = localizedObjectAnnotation.getName();
            BoundingPoly boundingPoly = localizedObjectAnnotation.getBoundingPoly();
            NormalizedVertex bottomLeft = boundingPoly.getNormalizedVertices(0);
            NormalizedVertex bottomRight = boundingPoly.getNormalizedVertices(1);
            NormalizedVertex topRight = boundingPoly.getNormalizedVertices(2);

            double width = Math.abs(bottomRight.getX() - bottomLeft.getX());
            double length = Math.abs(topRight.getY() - bottomRight.getY());
            if (name.equalsIgnoreCase("Plant")) {
                plant.setPoudaLength(length);
                plant.setPoudaWidth(width);
            } else {
                plant.setGhamlaLength(length);
                plant.setGhamlaWidth(width);
            }
        }

        if (plantFound == null) {
            Map<String, Object> response = PlantIdClient.getDetails(plant.getImageData());
            if (response.get("suggestions") != null) {
                plant.setType((String) (((Map<String, Object>) ((Map<String, Object>) ((List<Object>) response.get("suggestions")).get(0)).get("plant_details")).get("scientific_name")));
            }

            plantDAO.addPlant(plant);
        } else {
            plantDAO.updatePlantSize(plant);
        }
    }

    public Plant getPlantById(Long plantId) {
        return plantDAO.getPlant(plantId);
    }

    public List<Plant> getAllPlants(Long userId) {
        return plantDAO.getAllPlantsOfUser(userId);
    }

    public void deletePlant(Long plantId) throws CustomException {
        Plant plant = getPlantById(plantId);
        if (plant == null) {
            throw new CustomException(String.format("Plant with id:%s not found", plantId));
        }

        plantDAO.deletePlant(plantId);
    }

}
