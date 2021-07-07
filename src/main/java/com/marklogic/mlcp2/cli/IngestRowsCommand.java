package com.marklogic.mlcp2.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.marklogic.mlcp2.CommonConfig;
import com.marklogic.mlcp2.jdbc.IngestRowsConfig;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameter;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

@Parameters(commandDescription = "My description goes here")
public class IngestRowsCommand extends CommandSupport {

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
    public void runJob() throws JobExecutionException {
        // TODO Would be nice if there was an easy way - BeanWrapper? - to build this list of job parameters
        // Preferably, the parent class would do this automatically on anything annotated with Parameter
        Map<String, JobParameter> jobParams = new HashMap<>();
        jobParams.put("jdbc_driver", new JobParameter(jdbcDriver));
        jobParams.put("jdbc_url", new JobParameter(jdbcUrl));
        jobParams.put("jdbc_username", new JobParameter(jdbcUsername));
        jobParams.put("jdbc_password", new JobParameter(jdbcPassword));
        jobParams.put("sql", new JobParameter(sql));

        runJobWithParameters(jobParams, CommonConfig.class, IngestRowsConfig.class);
    }

    protected Map<String, JobParameter> buildJobParametersFromFields() {
        Map<String, JobParameter> jobParams = new HashMap<>();
        BeanWrapper bw = new BeanWrapperImpl(this);
        return jobParams;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }
}
