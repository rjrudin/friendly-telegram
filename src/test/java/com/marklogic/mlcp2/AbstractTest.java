package com.marklogic.mlcp2;

import com.marklogic.mgmt.ManageClient;
import com.marklogic.mgmt.ManageConfig;
import com.marklogic.mgmt.resource.databases.DatabaseManager;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractTest {

    @BeforeEach
    void clearDatabase() {
        ManageClient client = new ManageClient(new ManageConfig("localhost", 8002, "admin", "admin"));
        new DatabaseManager(client).clearDatabase("java-tester-content");
    }
}
