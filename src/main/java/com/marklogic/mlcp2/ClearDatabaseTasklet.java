package com.marklogic.mlcp2;

import com.marklogic.client.ext.helper.LoggingObject;
import com.marklogic.mgmt.ManageClient;
import com.marklogic.mgmt.resource.databases.DatabaseManager;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

public class ClearDatabaseTasklet extends LoggingObject implements Tasklet {

    @Autowired
    private ManageClient manageClient;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        new DatabaseManager(manageClient).clearDatabase("java-tester-content");
        return RepeatStatus.FINISHED;
    }
}
