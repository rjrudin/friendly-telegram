package com.marklogic.mlcp2.jdbc;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;
import com.marklogic.mlcp2.AbstractJobRunner;
import com.marklogic.mlcp2.CommonOptions;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameter;

import java.util.HashMap;
import java.util.Map;

/**
 * So should every command be a singleton? That allows for it to also be a Configuration class, where it can then
 * adds its properties to the Spring environment.
 */
@Parameters(commandDescription = "My description goes here")
public class IngestRowsJobRunner extends AbstractJobRunner {

    @ParametersDelegate
    private CommonOptions commonOptions = new CommonOptions();

    @Parameter(
        names = {"--jdbc_driver"},
        description = "TODO"
    )
    private String jdbcDriver;

    @Parameter(
        names = {"--jdbc_url"}
    )
    private String jdbcUrl;

    @Parameter(
        names = {"--jdbc_username"}
    )
    private String jdbcUsername;

    @Parameter(
        names = {"--jdbc_password"}
    )
    private String jdbcPassword;

    @Parameter(
        names = {"--sql"}
    )
    private String sql;

    @Override
    public JobExecution runJob() throws JobExecutionException {
        addCommonEnvironmentProperties(commonOptions);
        addEnvironmentProperty("jdbc_driver", jdbcDriver);
        addEnvironmentProperty("jdbc_url", jdbcUrl);
        addEnvironmentProperty("jdbc_username", jdbcUsername);
        addEnvironmentProperty("jdbc_password", jdbcPassword);

        Map<String, JobParameter> jobParams = new HashMap<>();
        jobParams.put("sql", new JobParameter(sql));
        jobParams.put("batch_size", new JobParameter(new Long(commonOptions.getBatchSize())));
        return runJobWithParameters(jobParams, IngestRowsConfig.class);
    }

    public CommonOptions getCommonOptions() {
        return commonOptions;
    }

    public void setCommonOptions(CommonOptions commonOptions) {
        this.commonOptions = commonOptions;
    }

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcUsername() {
        return jdbcUsername;
    }

    public void setJdbcUsername(String jdbcUsername) {
        this.jdbcUsername = jdbcUsername;
    }

    public String getJdbcPassword() {
        return jdbcPassword;
    }

    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}

