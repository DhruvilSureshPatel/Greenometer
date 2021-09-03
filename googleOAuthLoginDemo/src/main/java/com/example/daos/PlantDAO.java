package com.example.daos;

import com.example.models.Plant;
import com.example.models.User;
import com.google.inject.Inject;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class PlantDAO {

    private final Jdbi jdbi;

    @Inject
    public PlantDAO(Jdbi jdbi) {
        this.jdbi = jdbi;
        this.jdbi.registerRowMapper(new Plant.PlantMapper());
    }

    public Long addPlant(Plant plant) {
        return jdbi.withHandle(handle -> handle.createUpdate("INSERT INTO plants(user_id, name, type, " +
                        "o2_released, ghamla_length, ghamla_width, pouda_length, pouda_width, age) values (:user_id, :name, :type, " +
                        ":o2_released, :ghamla_length, :ghamla_width, :pouda_length, :pouda_width, :age)")
                .bind("user_id", plant.getUserId())
                .bind("name", plant.getName())
                .bind("type", plant.getType())
                .bind("o2_released", 0L)
                .bind("ghamla_length", plant.getGhamlaLength())
                .bind("ghamla_width", plant.getGhamlaWidth())
                .bind("pouda_length", plant.getPoudaLength())
                .bind("pouda_width", plant.getPoudaWidth())
                .bind("age", 0L)
                .executeAndReturnGeneratedKeys()
                .mapTo(Long.class).findFirst().orElse(null)
        );
    }

    public Plant getPlant(long id) {
        return jdbi.withHandle(handle -> handle.createQuery("select * from plants where id = :id and is_deleted = 0")
                .bind("id", id).mapTo(Plant.class).findFirst().orElse(null)
        );
    }

    public List<Plant> getAllPlantsOfUser(long userId) {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM plants where user_id = :userId and is_deleted = 0")
                .bind("userId", userId).mapTo(Plant.class).list()
        );
    }

    public void deletePlant(long id) {
        jdbi.withHandle(handle -> handle.createUpdate("UPDATE plants set is_deleted = 1 where id = :id")
                .bind("id", id).execute()
        );
    }

    public void deletePlantsOfAUser(long userId) {
        jdbi.withHandle(handle -> handle.createUpdate("UPDATE plants set is_deleted = 1 where user_id = :userId")
                .bind("userId", userId).execute()
        );
    }

    public void updatePlantSize(Plant plant) {
        jdbi.withHandle(handle -> handle.createUpdate("UPDATE plant set ghamla_length = :ghamlaLength, " +
                "ghamla_width = :ghamlaWidth, pouda_length = :poudaLength, pouda_width = :poudaWidth, o2_released = o2Released" +
                        " where id = :id and is_deleted = 0")
                .bind("ghamlaLength", plant.getGhamlaLength())
                .bind("ghamlaWidth", plant.getGhamlaWidth())
                .bind("poudaLength", plant.getPoudaLength())
                .bind("poudaWidth", plant.getPoudaWidth())
                .bind("o2_released", plant.getO2Released())
                .bind("id", plant.getId())
        );
    }

}
