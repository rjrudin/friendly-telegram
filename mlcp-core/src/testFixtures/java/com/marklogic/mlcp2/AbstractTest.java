package com.marklogic.mlcp2;

import com.marklogic.client.ext.helper.LoggingObject;
import com.marklogic.mgmt.ManageClient;
import com.marklogic.mgmt.ManageConfig;
import com.marklogic.mgmt.resource.databases.DatabaseManager;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractTest extends LoggingObject {

    @BeforeEach
    void clearDatabase() {
        // TODO Use server eval to do this so we don't need ml-app-deployer
        ManageClient client = new ManageClient(new ManageConfig("localhost", 8002, "admin", "admin"));
        new DatabaseManager(client).clearDatabase("java-tester-content");
    }

    protected void configureMarkLogicConnection(CommonOptions options) {
        options.setHost("localhost");
        options.setPort(8003);
        options.setUsername("admin");
        options.setPassword("admin");
    }
}
