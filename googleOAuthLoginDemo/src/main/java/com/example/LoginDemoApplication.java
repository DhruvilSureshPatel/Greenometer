package com.example;

import com.example.resources.HomePageResource;
import com.google.inject.Injector;
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
        bootstrap.addCommand(new MigrateCommand(this));
    }

    @Override
    public void run(final LoginDemoConfiguration configuration, final Environment environment) {

        Injector injector = BootstrapUtils.createGuiceInjector(configuration);
        injector.injectMembers(this);

        Module module = injector.getInstance(Module.class);
        module.initEnvironment(configuration, environment);

        environment.jersey().register(new HomePageResource());
    }

}
