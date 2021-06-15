package com.marklogic.mlcp2.repository.dao;

import java.util.Collection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marklogic.client.DatabaseClient;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.item.ExecutionContext;

public class MarkLogicExecutionContextDao implements ExecutionContextDao {

    private ObjectMapper objectMapper = new ObjectMapper();
    private DatabaseClient databaseClient;

    /**
     * return properties.getJobRepositoryDirectory() + "/" +
                jobExecution.getJobInstance().getId() + "/" +
                jobExecution.getId() + "/execution-context.xml";
     * @param databaseClient
     */
    public MarkLogicExecutionContextDao(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public ExecutionContext getExecutionContext(JobExecution jobExecution) {
        // String uri = "/executionContext/" + jobExecution.getId() + ".json";

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ExecutionContext getExecutionContext(StepExecution stepExecution) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void saveExecutionContext(JobExecution jobExecution) {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveExecutionContext(StepExecution stepExecution) {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveExecutionContexts(Collection<StepExecution> stepExecutions) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateExecutionContext(JobExecution jobExecution) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateExecutionContext(StepExecution stepExecution) {
        // TODO Auto-generated method stub

    }


}
