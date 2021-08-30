package com.marklogic.mlcp2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IngestFilesTest extends AbstractTest {

    @Test
    void test() {
        Main.main(new String[]{
            "ingestFiles",
            "--host", HOST,
            "--port", PORT + "",
            "--username", USERNAME,
            "--password", PASSWORD,
            "--input_file_path", "../mlcp-file/data/csv/**/*.csv"
        });

        assertEquals(9, getCollectionSize("mlcp-data"), "Expected 9 docs, 3 for each csv in the mlcp-file/data/csv directory");
    }
}
