package com.example.core.utils;

import com.example.LoginDemoConfiguration;
import com.google.crypto.tink.subtle.AesGcmJce;
import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import lombok.extern.slf4j.Slf4j;

import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Registry {

    private static Registry instance;

    private static Injector injector;

    public static Registry getInstance() {
        if (instance == null) {
            synchronized (Registry.class) {
                if (instance == null) {
                    instance = new Registry();
                }
            }
        }
        return instance;
    }

    public static void init(Injector inj) {
        if (injector == null) {
            synchronized (Registry.class) {
                if (injector == null) {
                    injector = inj;
                }
            }
        }
    }

    private final Map<Class<?>, Object> map;
    private final Map<Key, Object> typeLiteralMap;

    private Registry() {
        this.map = new HashMap<>();
        this.typeLiteralMap = new HashMap<>();
    }

    public <T> T register(T o) {
        return this.register((Class<T>) o.getClass(), o);
    }

    public <T> void register(TypeLiteral<T> typeLiteral, T o) {
        if (injector != null) {
            throw new RuntimeException("Use Guice Dependency injection instead of Registry for " + typeLiteral.getRawType());
        }
        this.typeLiteralMap.put(Key.get(typeLiteral),o);
    }

    public <T> T register(Class<T> clazz, T o) {
        if (injector != null) {
            throw new RuntimeException("Use Guice Dependency injection instead of Registry for " + clazz);
        }
        if (clazz.getName().contains("$$Enhancer")) {
            // Hack for mockito
            try {
                String className = clazz.getCanonicalName().substring(0, clazz.getCanonicalName().indexOf("$$Enhancer"));
                clazz = (Class<T>) Class.forName(className);
            } catch (ClassNotFoundException | ClassCastException e) {
                return null;
            }
        }
        if (clazz.isAssignableFrom(o.getClass())) {
            Object prev = this.map.putIfAbsent(clazz, o);
            return (prev == null) ? o : (T) prev;
        }
        throw new RuntimeException("Couldn't register " + clazz);
    }

    public <T> T get(Class<T> clazz) {
        try {
            return injector.getInstance(clazz);
        } catch (ConfigurationException | NullPointerException e) {
            Object o = this.map.get(clazz);
            if (clazz.isInstance(o)) {
                return (T) o;
            }
            if (injector != null) { // For main application only, not for tests
                throw new RuntimeException("No instance found in registry for " + clazz, e);
            } else {
                // Will print warning only for tests
                System.err.println("WARNING: No instance found in registry for " + clazz);
            }
            return null;
        }
    }

    public <T> T  getTypeLiteral(TypeLiteral<T> typeLiteral){
        Key<T> key = Key.get(typeLiteral);
        try {
            return injector.getInstance(key);
        } catch (ConfigurationException | NullPointerException e) {
            Object o = this.typeLiteralMap.get(key);
            if(typeLiteral.getRawType().isInstance(o)){
                return (T)o;
            }
            if (injector != null) { // For main application only, not for tests
                throw new RuntimeException("No instance found in registry for " + typeLiteral.getRawType(), e);
            } else {
                // Will print warning only for tests
                System.err.println("WARNING: No instance found in registry for " + typeLiteral.getRawType());
            }
            return null;
        }
    }

    public static <T> T getType(Class<T> type) {
        return getInstance().get(type);
    }

    public static LoginDemoConfiguration getConfig() {
        return getType(LoginDemoConfiguration.class);
    }

    public static AesGcmJce getMasterKeyCipher() throws GeneralSecurityException {
        LoginDemoConfiguration config = getConfig();
        byte[] key = Base64.getDecoder().decode(config.getConfigEncryptionMasterKey());
        return new AesGcmJce(key);
    }
}

