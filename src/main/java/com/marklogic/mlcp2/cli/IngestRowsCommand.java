package com.marklogic.mlcp2.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.marklogic.mlcp2.CommonConfig;
import com.marklogic.mlcp2.jdbc.IngestRowsConfig;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameter;

import java.util.HashMap;
import java.util.Map;

@Parameters(commandDescription = "My description goes here")
public class IngestRowsCommand extends CommandSupport {

    @Parameter(
        names = {"--jdbc_url"},
        description = "TODO"
    )
    private String jdbcUrl;

    @Override
    public void runJob() throws JobExecutionException {
        Map<String, JobParameter> jobParams = new HashMap<>();
        jobParams.put("jdbc_url", new JobParameter(jdbcUrl));

        runJobWithParameters(jobParams, CommonConfig.class, IngestRowsConfig.class);
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }
}
