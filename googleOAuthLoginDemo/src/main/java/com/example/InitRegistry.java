package com.example;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mysql.jdbc.Driver;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.beans.PropertyVetoException;

public class InitRegistry extends AbstractModule {

    private LoginDemoConfiguration config;
    private final ComboPooledDataSource dataSource;

    public InitRegistry(LoginDemoConfiguration configuration) {
        config = configuration;
        dataSource = createDataSource();
    }

    private ComboPooledDataSource createDataSource() {
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        try {
            comboPooledDataSource.setDriverClass(Driver.class.getCanonicalName()); //loads the jdbc driver
        } catch (PropertyVetoException e) {
            throw new RuntimeException("Error in setting db driver", e);
        }
        comboPooledDataSource.setJdbcUrl(config.getDbURL());
        comboPooledDataSource.setMinPoolSize(5);
        comboPooledDataSource.setMaxPoolSize(10);
        comboPooledDataSource.setTestConnectionOnCheckout(true);
        comboPooledDataSource.setPreferredTestQuery("SELECT 1");
        return comboPooledDataSource;
    }

    @Provides
    @Singleton
    public Jdbi jdbi3() {
        Jdbi jdbi = Jdbi.create(dataSource);
        jdbi.installPlugin(new SqlObjectPlugin());
        return jdbi;
    }

}
