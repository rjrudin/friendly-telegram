package com.marklogic.mlcp2;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;

/**
 * Exists solely to allow for a method to throw a JobExecutionException.
 */
public interface JobRunner {

    JobExecution runJob() throws JobExecutionException;
}
