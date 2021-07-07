package com.marklogic.mlcp2;

import com.marklogic.mlcp2.cli.Main;
import org.junit.jupiter.api.Test;

public class RunCustomJobTest extends AbstractTest {

    @Test
    void test() {
        Main.main(new String[]{
            "--username", "admin",
            "--password", "admin",
            "custom",
            "--class_name", "org.example.CustomConfigExample",
            "-Dmy_limit=12"
        });
    }
}
