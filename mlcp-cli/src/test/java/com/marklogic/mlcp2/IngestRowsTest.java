package com.marklogic.mlcp2;

import org.junit.jupiter.api.Test;

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
            "--jdbc_driver", "org.h2.Driver",
            "--jdbc_url", "jdbc:h2:file:./data/h2/sample",
            "--jdbc_username", "sa",
            "--jdbc_password", "",
            "--sql", "SELECT * FROM Customer"
        });
    }
}
