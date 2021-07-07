package com.marklogic.mlcp2.cli;

import com.beust.jcommander.Parameter;

import java.util.HashMap;
import java.util.Map;

public class CommonOptions {

    @Parameter(
        names = {"--host"},
        required = true,
        description = "TODO"
    )
    String host;

    @Parameter(
        names = {"--port"},
        required = true,
        description = "TODO"
    )
    int port;

    @Parameter(
        names = {"--managePort"},
        description = "TODO"
    )
    int managePort = 8002;

    @Parameter(
        names = {"--username"},
        required = true,
        description = "TODO"
    )
    String username;

    @Parameter(
        names = {"--password"},
        required = true,
        password = true,
        description = "TODO"
    )
    String password;

    @Parameter(names = {"--batch_size"}, required = false)
    Integer batchSize = 100;

    public Map<String, Object> getEnvironmentProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("host", host);
        props.put("port", port);
        props.put("username", username);
        props.put("password", password);
        return props;
    }
}
