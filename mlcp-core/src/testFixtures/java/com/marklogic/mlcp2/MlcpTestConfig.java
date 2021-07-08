package com.marklogic.mlcp2;

import com.marklogic.client.ext.DatabaseClientConfig;
import com.marklogic.client.ext.helper.DatabaseClientProvider;
import com.marklogic.client.ext.spring.SimpleDatabaseClientProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
public class MlcpTestConfig {

    /**
     * Has to be static so that Spring instantiates it first.
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        PropertySourcesPlaceholderConfigurer c = new PropertySourcesPlaceholderConfigurer();
        c.setIgnoreResourceNotFound(true);
        return c;
    }

    /**
     * Defines the configuration details for constructing a DatabaseClient. Assumes the use of digest authentication.
     * Can subclass and override this method to define a different authentication strategy.
     *
     * @return
     */
    @Bean
    public DatabaseClientConfig databaseClientConfig() {
        // TODO Refactor to gradle.properties, once we have an ml-gradle app in place
        return new DatabaseClientConfig("localhost", 8003, "admin", "admin");
    }

    /**
     * AbstractSpringMarkLogicTest depends on an instance of DatabaseClientProvider.
     *
     * @return
     */
    @Bean
    public DatabaseClientProvider databaseClientProvider() {
        return new SimpleDatabaseClientProvider(databaseClientConfig());
    }

}
