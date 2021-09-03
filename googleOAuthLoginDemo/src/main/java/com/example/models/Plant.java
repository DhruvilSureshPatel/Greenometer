package com.example.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Plant {

    private Long id;
    private Long userId;
    @Length(max = 45, message = "length must be lesser than or equal to {max}")
    private String name;
    private String type;
    private String imageData;
    private long age;
    private long o2Released;
    private double ghamlaLength;
    private double ghamlaWidth;
    private double poudaLength;
    private double poudaWidth;
    private boolean isDeleted;

    public static class PlantMapper implements RowMapper<Plant> {

        @Override
        public Plant map(ResultSet rs, StatementContext ctx) throws SQLException {
            return Plant.builder()
                    .id(rs.getLong("id"))
                    .userId(rs.getLong("user_id"))
                    .name(rs.getString("name"))
                    .type(rs.getString("type"))
                    .imageData(rs.getString("imageData"))
                    .age(rs.getLong("age"))
                    .o2Released(rs.getLong("o2_released"))
                    .ghamlaLength(rs.getDouble("ghamla_length"))
                    .ghamlaWidth(rs.getDouble("ghamla_width"))
                    .poudaLength(rs.getDouble("pouda_length"))
                    .poudaWidth(rs.getDouble("pouda_width"))
                    .isDeleted(rs.getBoolean("is_deleted"))
                    .build();
        }
    }
}
