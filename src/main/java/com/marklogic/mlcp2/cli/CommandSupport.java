package com.marklogic.mlcp2.cli;

import com.marklogic.client.ext.helper.LoggingObject;
import com.marklogic.mlcp2.EnvironmentPropertySource;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

/**
 * Not sure if this will hold us as having value, but creating it now to avoid duplication.
 */
public abstract class CommandSupport extends LoggingObject implements JobCommand {

    protected CommandSupport addCommonEnvironmentProperties(CommonOptions options) {
        EnvironmentPropertySource.environmentProperties.putAll(options.getEnvironmentProperties());
        return this;
    }

    protected CommandSupport addEnvironmentProperty(String name, Object value) {
        EnvironmentPropertySource.environmentProperties.put(name, value);
        return this;
    }

    protected JobExecution runJobWithParameters(Map<String, JobParameter> jobParameterMap, Class<?>... configurationClasses) throws JobExecutionException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(configurationClasses);

        JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
        Job job = ctx.getBean(Job.class);

        JobExecution jobExecution = jobLauncher.run(job, new JobParameters(jobParameterMap));
        logger.info("JobExecution: " + jobExecution);
        logger.info("ExecutionContext: " + jobExecution.getExecutionContext());
        logger.info("JobInstance: " + jobExecution.getJobInstance());
        return jobExecution;
    }
}
