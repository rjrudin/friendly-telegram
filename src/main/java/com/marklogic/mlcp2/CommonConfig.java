package com.marklogic.mlcp2;

import com.marklogic.client.ext.DatabaseClientConfig;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;

@EnableBatchProcessing
public class CommonConfig {

    @Autowired
    ConfigurableEnvironment environment;

    public static Properties environmentProperties = new Properties();

    @Bean
    @Lazy(false)
    public PropertiesPropertySource commonEnvironmentProperties() {
        System.out.println("Hi from CommonConfig!");
        PropertiesPropertySource source = new PropertiesPropertySource("commonEnvironmentProperties", environmentProperties);
        environment.getPropertySources().addFirst(source);
        return source;
    }

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
