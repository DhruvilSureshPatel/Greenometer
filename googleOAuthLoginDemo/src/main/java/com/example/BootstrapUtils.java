package com.example;

import com.example.core.utils.Registry;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class BootstrapUtils {

    public static Injector createGuiceInjector(LoginDemoConfiguration config) {
        Injector injector = Guice.createInjector(new InitRegistry(config));
        Registry.init(injector);
        return injector;
    }

}
