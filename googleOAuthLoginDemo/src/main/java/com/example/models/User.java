package com.example.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.security.Principal;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Principal {

    private long id;
    @Length(max = 45,
            min = 5,
            message = "length must be lesser than or equal to {max} and greater than {min}")
    @NotBlank(message = "must be specified")
    private String name;
    @Length(max = 45,
            min = 5,
            message = "length must be lesser than or equal to {max} and greater than {min}")
    @NotBlank(message = "must be specified")
    private String email;
    @Length(max = 45,
            min = 5,
            message = "length must be lesser than or equal to {max} and greater than {min}")
    @NotBlank(message = "must be specified")
    private String password;
    private boolean isDeleted;
    private String token;
    private long rewardPoints;

    public static class UserMapper implements RowMapper<User> {

        @Override
        public User map(ResultSet rs, StatementContext ctx) throws SQLException {
            return User.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .email(rs.getString("email"))
                    .password(rs.getString("password"))
                    .isDeleted(rs.getBoolean("is_deleted"))
                    .token(rs.getString("token"))
                    .rewardPoints(rs.getLong("reward_points"))
                    .build();
        }
    }

}
