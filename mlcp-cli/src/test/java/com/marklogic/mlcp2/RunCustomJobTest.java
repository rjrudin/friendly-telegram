package com.marklogic.mlcp2;

import org.junit.jupiter.api.Test;

public class RunCustomJobTest extends AbstractTest {

    @Test
    void test() {
        Main.main(new String[]{
            "custom",
            "--host", "localhost",
            "--port", "8003",
            "--username", "admin",
            "--password", "admin",
            "--class_name", "org.example.CustomConfigExample",
            "-Dmy_limit=12"
        });
    }
}
