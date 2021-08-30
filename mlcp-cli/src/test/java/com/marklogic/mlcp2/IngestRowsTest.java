package com.marklogic.mlcp2;

import org.h2.tools.RunScript;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IngestRowsTest extends AbstractTest {

    @BeforeEach
    void beforeEach() throws Exception {
        // Setup a sample h2 database for this test
        RunScript.main(
            "-url", "jdbc:h2:file:./build/h2-sample-db",
            "-user", "sa",
            "-script", "../mlcp-jdbc/data/setup-h2sample-db.sql"
        );
    }

    @Test
    void test() {
        Main.main(new String[]{
            "ingestRows",
            "--host", HOST,
            "--port", PORT + "",
            "--username", USERNAME,
            "--password", PASSWORD,
            "--batch_size", "50",
            "--jdbc_driver", "org.h2.Driver",
            "--jdbc_url", "jdbc:h2:file:./build/h2-sample-db",
            "--jdbc_username", "sa",
            "--jdbc_password", "",
            "--sql", "SELECT * FROM Customer"
        });

        assertEquals(50, getCollectionSize("mlcp-data"), "Expected 50 docs, one for each row in the Customer table");
    }
}
