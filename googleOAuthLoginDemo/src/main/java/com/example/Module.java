package com.example;

import com.example.resources.TestResource;
import com.example.resources.UserResource;
import com.google.inject.Inject;
import io.dropwizard.setup.Environment;

public class Module {

    private TestResource testResource;
    private UserResource userResource;

    @Inject
    public Module(TestResource testResource, UserResource userResource) {
        this.testResource = testResource;
        this.userResource = userResource;
    }

    public void initEnvironment(LoginDemoConfiguration configuration, Environment environment) {
        environment.jersey().register(testResource);
        environment.jersey().register(userResource);
    }

}
