package com.marklogic.mlcp2;

import org.springframework.batch.core.JobExecutionException;

public interface JobCommand {

    void runJob() throws JobExecutionException;
}
