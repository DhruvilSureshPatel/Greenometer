package com.example;

import com.example.resources.TestResource;
import com.google.inject.Inject;
import io.dropwizard.setup.Environment;

public class Module {

    private TestResource testResource;

    @Inject
    public Module(TestResource testResource) {
        this.testResource = testResource;
    }

    public void initEnvironment(LoginDemoConfiguration configuration, Environment environment) {
        environment.jersey().register(testResource);
    }

}
