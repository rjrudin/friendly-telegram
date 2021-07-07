package com.marklogic.mlcp2.cli;

import org.springframework.batch.core.JobExecutionException;

/**
 * Exists solely to allow for a method to throw a JobExecutionException.
 */
public interface JobCommand {

    void runJob() throws JobExecutionException;
}
