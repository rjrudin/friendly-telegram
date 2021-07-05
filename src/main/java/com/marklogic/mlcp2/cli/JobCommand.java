package com.marklogic.mlcp2.cli;

import org.springframework.batch.core.JobExecutionException;

public interface JobCommand {

    void runJob() throws JobExecutionException;
}
