package com.marklogic.mlcp2;

import com.marklogic.client.ext.DatabaseClientConfig;
import com.marklogic.client.ext.helper.DatabaseClientProvider;
import com.marklogic.client.ext.spring.SimpleDatabaseClientProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MlcpTestConfig {

    @Bean
    public DatabaseClientConfig databaseClientConfig() {
        return new DatabaseClientConfig(AbstractTest.HOST, AbstractTest.PORT, AbstractTest.USERNAME, AbstractTest.PASSWORD);
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
