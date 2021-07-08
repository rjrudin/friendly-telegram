package com.marklogic.mlcp2;

import com.marklogic.client.ext.DatabaseClientConfig;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;

/**
 * Defines a base configuration for MLCP batch jobs.
 */
@EnableBatchProcessing
@Lazy
public class BaseConfig {

    @Autowired
    Environment environment;

    @Bean
    // Ensures this is immediately created so that its properties can be immediately added to the Spring environment
    @Lazy(false)
    public EnvironmentPropertySource environmentPropertySource() {
        return new EnvironmentPropertySource();
    }

    @Bean
    public DatabaseClientConfig databaseClientConfig() {
        // TODO Add support for other properties
        DatabaseClientConfig config = new DatabaseClientConfig();
        config.setHost(environment.getProperty("host"));
        config.setPort(environment.getProperty("port", Integer.class));
        config.setUsername(environment.getProperty("username"));
        config.setPassword(environment.getProperty("password"));
        return config;
    }
}
