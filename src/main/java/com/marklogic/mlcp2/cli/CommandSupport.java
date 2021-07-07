package com.marklogic.mlcp2.cli;

import com.marklogic.client.ext.helper.LoggingObject;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

/**
 * Not sure if this will hold us as having value, but creating it to avoid duplication.
 */
public abstract class CommandSupport extends LoggingObject implements JobCommand {

    protected JobExecution runJobWithParameters(Map<String, JobParameter> jobParameterMap, Class<?>... configurationClasses) throws JobExecutionException {
        // CliConfig handles adding a new Spring properties source
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
            ArrayUtils.addFirst(configurationClasses, CliConfig.class)
        );

        JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
        Job job = ctx.getBean(Job.class);

        JobExecution jobExecution = jobLauncher.run(job, new JobParameters(jobParameterMap));
        logger.info("JobExecution: " + jobExecution);
        logger.info("ExecutionContext: " + jobExecution.getExecutionContext());
        logger.info("JobInstance: " + jobExecution.getJobInstance());
        return jobExecution;
    }

}
