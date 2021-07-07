package com.marklogic.mlcp2;

import com.marklogic.client.ext.DatabaseClientConfig;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@EnableBatchProcessing
public class CommonConfig {

    @Autowired
    Environment environment;

    @Bean
    public DatabaseClientConfig databaseClientConfig() {
        DatabaseClientConfig config = new DatabaseClientConfig();
        config.setHost(environment.getProperty("host"));
        config.setPort(environment.getProperty("port", Integer.class));
        config.setUsername(environment.getProperty("username"));
        config.setPassword(environment.getProperty("password"));
        return config;
    }
}
