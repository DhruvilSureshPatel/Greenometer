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

    private long id;
    private long userId;
    @Length(max = 45, message = "length must be lesser than or equal to {max}")
    private String name;
    private String type;
    private String imageData;
    private long o2Released = 2L;
    private long ghamlaLength = 5;
    private long ghamlaWidth = 6;
    private long poudaLength = 8;
    private long poudaWidth = 8;
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
                    .o2Released(rs.getLong("o2_released"))
                    .ghamlaLength(rs.getLong("ghamla_length"))
                    .ghamlaWidth(rs.getLong("ghamla_width"))
                    .poudaLength(rs.getLong("pouda_length"))
                    .poudaWidth(rs.getLong("pouda_width"))
                    .isDeleted(rs.getBoolean("is_deleted"))
                    .build();
        }
    }
}
