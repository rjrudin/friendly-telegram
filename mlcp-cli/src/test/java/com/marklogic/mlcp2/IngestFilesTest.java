package com.marklogic.mlcp2;

import org.junit.jupiter.api.Test;

public class IngestFilesTest extends AbstractTest {

    @Test
    void test() {
        Main.main(new String[]{
            "ingestFiles",
            "--host", HOST,
            "--port", PORT + "",
            "--username", USERNAME,
            "--password", PASSWORD,
            "--input_file_path", "data/csv/**/*.csv"
            //, "--input_file_path", "data/csv/customers1.csv"
        });
    }
}
