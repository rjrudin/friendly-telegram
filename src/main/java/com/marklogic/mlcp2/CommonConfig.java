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
        CommonOptions options = CommonOptions.getInstance();
        return new ManageClient(new ManageConfig(options.getHost(), 8002, options.getUsername(), options.getPassword()));
    }

    @Bean
    public DatabaseClientConfig databaseClientConfig() {
        CommonOptions options = CommonOptions.getInstance();

        DatabaseClientConfig config = new DatabaseClientConfig();
        config.setHost(options.getHost());
        config.setPort(options.getPort());
        config.setUsername(options.getUsername());
        config.setPassword(options.getPassword());
        return config;
    }
}
