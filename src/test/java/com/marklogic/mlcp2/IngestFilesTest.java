package com.marklogic.mlcp2;

import com.marklogic.mlcp2.cli.Main;

public class IngestFilesTest {

    public static void main(String[] args) {
        Main.main(new String[]{
            "--username", "admin",
            "--password", "admin",
            "ingestFiles",
            "--input_file_path", "data/csv/**/*.csv"
            //, "--input_file_path", "data/csv/customers1.csv"
        });
    }
}
