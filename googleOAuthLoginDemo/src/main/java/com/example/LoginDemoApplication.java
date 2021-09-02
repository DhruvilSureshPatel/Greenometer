package com.example;

import com.example.resources.HomePageResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class LoginDemoApplication extends Application<LoginDemoConfiguration> {

    public static void main(final String[] args) throws Exception {
        new LoginDemoApplication().run(args);
    }

    @Override
    public String getName() {
        return "LoginDemo";
    }

    @Override
    public void initialize(final Bootstrap<LoginDemoConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final LoginDemoConfiguration configuration, final Environment environment) {
        environment.jersey().register(new HomePageResource());
    }

}
