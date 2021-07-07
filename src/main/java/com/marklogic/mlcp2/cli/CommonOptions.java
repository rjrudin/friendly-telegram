package com.marklogic.mlcp2.cli;

import com.beust.jcommander.Parameter;

import java.util.Properties;

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
    private Integer batchSize = 100;

    public Properties getEnvironmentProperties() {
        Properties props = new Properties();
        props.setProperty("host", host);
        props.setProperty("port", port + "");
        props.setProperty("username", username);
        props.setProperty("password", password);
        return props;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }
}
