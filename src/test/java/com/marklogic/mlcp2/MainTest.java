package com.marklogic.mlcp2;

import org.junit.jupiter.api.Test;

public class MainTest {

    @Test
    void test() {
        String[] args = new String[] { "--username", "admin", "--password", "admin", "ingestRows", "--jdbc_url",
                "jdbc:h2:file:./data/h2/sample"
                // ,"ingestFiles"
                // . "--input_file_path", "data/csv/customers1.csv"
        };

        Main.main(args);
    }
}
