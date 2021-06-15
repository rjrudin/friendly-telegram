package com.marklogic.mlcp2;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.Map;

@Parameters(commandDescription = "My description goes here")
public class IngestRowsCommand implements JobCommand {

    @Parameter(
        names = {"--jdbc_url"},
        description = "TODO"
    )
    private String jdbcUrl;

    @Override
    public void runJob() throws JobExecutionException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
            CommonConfig.class,
            IngestRowsConfig.class
        );

        JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
        Job job = ctx.getBean(Job.class);

        Map<String, JobParameter> jobParams = new HashMap<>();
        jobParams.put("jdbc_url", new JobParameter(jdbcUrl));
        JobExecution jobExecution = jobLauncher.run(job, new JobParameters(jobParams));
        System.out.println(jobExecution);
        System.out.println(jobExecution.getExecutionContext());
        System.out.println(jobExecution.getJobInstance());
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }
}
