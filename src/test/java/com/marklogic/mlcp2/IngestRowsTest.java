package com.marklogic.mlcp2;

import com.marklogic.mlcp2.cli.Main;

public class IngestRowsTest {

    public static void main(String[] args) {
        Main.main(new String[]{
            "--username", "admin",
            "--password", "admin",
            "ingestRows",
            "--jdbc_url", "jdbc:h2:file:./data/h2/sample"
        });
    }

}
