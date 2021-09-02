package com.example;

import com.example.core.exceptions.CustomException;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import io.dropwizard.Application;
import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.setup.Bootstrap;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.flywaydb.core.Flyway;

@Slf4j
public class MigrateCommand extends ConfiguredCommand<LoginDemoConfiguration> {

    private final Class<LoginDemoConfiguration> configurationClass;

    MigrateCommand(Application<LoginDemoConfiguration> application) {
        super("migrate", "Runs flyway db migration");
        this.configurationClass = application.getConfigurationClass();
    }

    @Override
    protected Class<LoginDemoConfiguration> getConfigurationClass() {
        return configurationClass;
    }

    @Override
    protected void run(Bootstrap<LoginDemoConfiguration> bootstrap, Namespace namespace, LoginDemoConfiguration config) {
        try {
            Flyway flyway = new Flyway();
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setURL(config.getDbURL());
            flyway.setDataSource(dataSource);
            flyway.setLocations("migration");

            String action = namespace.getString("action");
            switch (action) {
                case "migrate":
                    flyway.migrate();
                    break;
                case "info":
                    flyway.info();
                    break;
                case "repair":
                    flyway.repair();
                    break;
                default:
                    throw new CustomException("Invalid command: " + action);
            }

            BootstrapUtils.createGuiceInjector(config);

            log.info("ran " + action + " successfully!");
            System.exit(0);
        } catch (Exception e) {
            log.error("failed to run MigrateCommand", e);
            System.exit(1);
        }
    }

    @Override
    public void configure(Subparser subparser) {
        super.configure(subparser);
        subparser.addArgument("-a", "--action")
                .dest("action")
                .type(String.class)
                .required(true)
                .help("action to perform migrate, info or repair");
    }
}
