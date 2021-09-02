package com.example;

import com.example.models.User;
import com.example.resources.PlantResource;
import com.example.resources.TestResource;
import com.example.resources.UserResource;
import com.google.inject.Inject;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

public class Module {

    private TestResource testResource;
    private UserResource userResource;
    private PlantResource plantResource;

    @Inject
    public Module(TestResource testResource, UserResource userResource, PlantResource plantResource) {
        this.testResource = testResource;
        this.userResource = userResource;
        this.plantResource = plantResource;
    }

    public void initEnvironment(LoginDemoConfiguration configuration, Environment environment) {
        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(new UserAuthenticator())
                        .setAuthorizer(new UserAuthorizer())
                        .setRealm("SUPER SECRET STUFF")
                        .buildAuthFilter())
        );
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));

        environment.jersey().register(testResource);
        environment.jersey().register(userResource);
        environment.jersey().register(plantResource);
    }

}
