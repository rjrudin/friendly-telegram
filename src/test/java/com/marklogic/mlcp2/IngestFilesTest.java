package com.marklogic.mlcp2;

import com.marklogic.mlcp2.cli.Main;
import org.junit.jupiter.api.Test;

public class IngestFilesTest extends AbstractTest {

    @Test
    void test() {
        Main.main(new String[]{
            "--username", "admin",
            "--password", "admin",
            "ingestFiles",
            "--input_file_path", "data/csv/**/*.csv"
            //, "--input_file_path", "data/csv/customers1.csv"
        });
    }
}
