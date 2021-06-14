package com.marklogic.mlcp2;

import com.marklogic.client.ext.DatabaseClientConfig;
import com.marklogic.mgmt.ManageClient;
import com.marklogic.mgmt.ManageConfig;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Bean;

@EnableBatchProcessing
public class CommonConfig {

    @Bean
    public ManageClient manageClient() {
        return new ManageClient(new ManageConfig("localhost", 8002, "admin", "admin"));
    }

    @Bean
    public DatabaseClientConfig databaseClientConfig() {
        DatabaseClientConfig config = new DatabaseClientConfig();
        config.setHost("localhost");
        config.setPort(8003);
        config.setUsername("admin");
        config.setPassword("admin");
        return config;
    }
}
