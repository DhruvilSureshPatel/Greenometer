package com.example;

import io.dropwizard.Configuration;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.*;

public class LoginDemoConfiguration extends Configuration {

    @Valid
    @NotNull
    @Getter
    private String dbURL;

    @Valid
    @NotNull
    @Getter
    private String configEncryptionMasterKey;

}
