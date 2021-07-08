package com.marklogic.mlcp2;

import com.beust.jcommander.Parameter;

import java.util.HashMap;
import java.util.Map;

public class CommonOptions {

    @Parameter(
        names = {"--host"},
        required = true,
        description = "TODO"
    )
    private String host;

    @Parameter(
        names = {"--port"},
        required = true,
        description = "TODO"
    )
    private int port;

    @Parameter(
        names = {"--managePort"},
        description = "TODO"
    )
    private int managePort = 8002;

    @Parameter(
        names = {"--username"},
        required = true,
        description = "TODO"
    )
    private String username;

    @Parameter(
        names = {"--password"},
        required = true,
        password = true,
        description = "TODO"
    )
    private String password;

    @Parameter(names = {"--batch_size"}, required = false)
    private int batchSize = 100;

    public Map<String, Object> getEnvironmentProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("host", host);
        props.put("port", port);
        props.put("username", username);
        props.put("password", password);
        return props;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getManagePort() {
        return managePort;
    }

    public void setManagePort(int managePort) {
        this.managePort = managePort;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}
