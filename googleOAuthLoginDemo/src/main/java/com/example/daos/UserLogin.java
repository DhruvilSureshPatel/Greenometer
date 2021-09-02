package com.example.daos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLogin {

    @Length(max = 45,
            message = "Email length must be lesser than or equal to {max}")
    @NotBlank(message = "Email must be specified")
    private String email;
    @Length(max = 45,
            message = "Password length must be lesser than or equal to {max}")
    @NotBlank(message = "Password must be specified")
    private String password;

}
