package com.marklogic.mlcp2.cli;

import com.marklogic.mlcp2.AbstractTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IngestRowsTest extends AbstractTest {

    @Test
    void test() {
        Main.main(new String[]{
            "ingestRows",
            "--host", HOST,
            "--port", PORT + "",
            "--username", USERNAME,
            "--password", PASSWORD,
            "--batch_size", "50",
            "--jdbc_driver_path", "../data/h2/h2-1.4.200.jar",
            "--jdbc_driver", "org.h2.Driver",
            "--jdbc_url", "jdbc:h2:file:../data/h2/h2-sample-db",
            "--jdbc_username", "sa",
            "--jdbc_password", "",
            "--sql", "SELECT * FROM Customer"
        });

        assertEquals(50, getCollectionSize("mlcp-data"), "Expected 50 docs, one for each row in the Customer table");
    }
}
