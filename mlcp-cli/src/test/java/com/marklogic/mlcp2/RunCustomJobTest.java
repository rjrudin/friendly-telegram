package com.marklogic.mlcp2;

import org.junit.jupiter.api.Test;

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
    }
}
