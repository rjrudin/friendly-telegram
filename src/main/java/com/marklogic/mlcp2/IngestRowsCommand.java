package com.marklogic.mlcp2;

import com.beust.jcommander.Parameters;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.Map;

@Parameters(commandDescription = "My description goes here")
public class IngestRowsCommand implements JobCommand {

    @Override
    public void runJob() throws JobExecutionException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
                CommonConfig.class,
                IngestRowsConfig.class
        );

        JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
        Job job = ctx.getBean(Job.class);

        Map<String, JobParameter> jobParams = new HashMap<>();
        JobExecution jobExecution = jobLauncher.run(job, new JobParameters(jobParams));
        System.out.println(jobExecution);
    }
}
