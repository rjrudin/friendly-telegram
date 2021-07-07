package com.marklogic.mlcp2.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;
import com.marklogic.mlcp2.CommonConfig;
import com.marklogic.mlcp2.jdbc.IngestRowsConfig;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameter;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * So should every command be a singleton? That allows for it to also be a Configuration class, where it can then
 * adds its properties to the Spring environment.
 */
@Parameters(commandDescription = "My description goes here")
public class IngestRowsCommand extends CommandSupport {

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

    public Properties getEnvironmentProperties() {
        Properties props = new Properties();
        props.putAll(commonOptions.getEnvironmentProperties());
        props.setProperty("jdbcDriver", jdbcDriver);
        props.setProperty("jdbcUrl", jdbcUrl);
        props.setProperty("jdbcUsername", jdbcUsername);
        props.setProperty("jdbcPassword", jdbcPassword);
        return props;
    }

    @Override
    public void runJob() throws JobExecutionException {
        Map<String, JobParameter> jobParams = new HashMap<>();
        jobParams.put("sql", new JobParameter(sql));
        System.out.println("BS: " + commonOptions.getBatchSize());
        jobParams.put("batch_size", new JobParameter(new Long(commonOptions.getBatchSize())));

        CommonConfig.environmentProperties.putAll(getEnvironmentProperties());
        runJobWithParameters(jobParams, CommonConfig.class, IngestRowsConfig.class);
    }

}

