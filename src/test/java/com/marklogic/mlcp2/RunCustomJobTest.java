package com.marklogic.mlcp2;

import com.marklogic.mlcp2.cli.Main;

public class RunCustomJobTest {

    public static void main(String[] args) {
        Main.main(new String[]{
            "--username", "admin",
            "--password", "admin",
            "custom",
            "--class_name", "org.example.CustomConfigExample",
            "-Dmy_limit=12"
        });
    }
}
