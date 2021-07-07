package com.marklogic.mlcp2;

import com.marklogic.mlcp2.cli.Main;
import org.junit.jupiter.api.Test;

public class IngestRowsTest extends AbstractTest {

    @Test
    void test() {
        Main.main(new String[]{
            "--username", "admin",
            "--password", "admin",
            "ingestRows",
            "--jdbc_driver", "org.h2.Driver",
            "--jdbc_url", "jdbc:h2:file:./data/h2/sample",
            "--jdbc_username", "sa",
            "--jdbc_password", "",
            "--sql", "SELECT * FROM Customer"
        });
    }
}
