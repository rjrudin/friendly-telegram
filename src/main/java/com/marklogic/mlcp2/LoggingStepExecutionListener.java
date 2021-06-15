package com.marklogic.mlcp2;

import com.marklogic.client.ext.helper.LoggingObject;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class LoggingStepExecutionListener extends LoggingObject implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        logger.info("Before: " + stepExecution);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        logger.info("After: " + stepExecution);
        return stepExecution.getExitStatus();
    }
}
