package com.marklogic.mlcp2.cli;

import com.marklogic.mlcp2.AbstractTest;
import com.marklogic.mlcp2.cli.Main;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RunCustomJobTest extends AbstractTest {

    @Test
    void test() {
        Main.main(new String[]{
            "custom",
            "--host", HOST,
            "--port", PORT + "",
            "--username", USERNAME,
            "--password", PASSWORD,
            "--class_name", "org.example.CustomConfigExample",
            "-Dmy_limit=12"
        });

        assertEquals(12, getCollectionSize("mlcp-data"), "Expecting 12 docs since my_limit=12 in the args passed " +
            "to the custom config class");
    }
}
