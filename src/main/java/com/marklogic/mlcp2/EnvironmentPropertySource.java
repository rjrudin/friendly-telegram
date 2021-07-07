package com.marklogic.mlcp2;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Allows for environment properties to be added by a client before instantiating a Spring container. An instance of
 * this class can then be included in a Spring configuration class, and it will immediately add all of its properties
 * to the Spring environment.
 * <p>
 * Environment properties can be added via the public and static Properties field.
 */
public class EnvironmentPropertySource {

    public static Map<String, Object> environmentProperties = new HashMap<>();

    @Autowired
    ConfigurableEnvironment environment;

    @PostConstruct
    public void addPropertiesToEnvironment() {
        if (environmentProperties.size() > 0) {
            LoggerFactory.getLogger(getClass()).info("Adding properties to Spring environment; property count: " + environmentProperties.size());
            environment.getPropertySources().addFirst(new MapPropertySource("commonEnvironmentProperties", environmentProperties));
        }
    }
}
