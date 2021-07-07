package com.marklogic.mlcp2.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;
import com.marklogic.mlcp2.jdbc.IngestRowsConfig;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameter;

import java.util.HashMap;
import java.util.Map;

/**
 * So should every command be a singleton? That allows for it to also be a Configuration class, where it can then
 * adds its properties to the Spring environment.
 */
@Parameters(commandDescription = "My description goes here")
public class IngestRowsCommand extends CommandSupport {

    @ParametersDelegate
    CommonOptions commonOptions = new CommonOptions();

    @Parameter(
        names = {"--jdbc_driver"},
        description = "TODO"
    )
    String jdbcDriver;

    @Parameter(
        names = {"--jdbc_url"}
    )
    String jdbcUrl;

    @Parameter(
        names = {"--jdbc_username"}
    )
    String jdbcUsername;

    @Parameter(
        names = {"--jdbc_password"}
    )
    String jdbcPassword;

    @Parameter(
        names = {"--sql"}
    )
    String sql;

    @Override
    public void runJob() throws JobExecutionException {
        addCommonEnvironmentProperties(commonOptions)
            .addEnvironmentProperty("jdbc_driver", jdbcDriver)
            .addEnvironmentProperty("jdbc_url", jdbcUrl)
            .addEnvironmentProperty("jdbc_username", jdbcUsername)
            .addEnvironmentProperty("jdbc_password", jdbcPassword);

        Map<String, JobParameter> jobParams = new HashMap<>();
        jobParams.put("sql", new JobParameter(sql));
        jobParams.put("batch_size", new JobParameter(new Long(commonOptions.batchSize)));
        runJobWithParameters(jobParams, IngestRowsConfig.class);
    }
}

