package com.marklogic.mlcp2.jdbc;

import com.marklogic.mlcp2.AbstractTest;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IngestRowsNoCliTest extends AbstractTest {

    @Test
    void test() throws Exception {
        IngestRowsJobRunner runner = new IngestRowsJobRunner();
        configureMarkLogicConnection(runner.getCommonOptions());

        runner.setJdbcDriverPath("../data/h2/h2-1.4.200.jar");
        runner.setJdbcDriver("org.h2.Driver");
        runner.setJdbcUrl("jdbc:h2:file:../data/h2/h2-sample-db");
        runner.setJdbcUsername("sa");
        runner.setJdbcPassword("");
        runner.setSql("SELECT * FROM ITEM");

        JobExecution jobExecution = runner.runJob();
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

        String count = getDatabaseClient().newServerEval().javascript("cts.estimate(cts.collectionQuery('mlcp-data'))").evalAs(String.class);
        assertEquals(650, Integer.parseInt(count), "Expected 650 docs, as there are 650 rows in the ITEM table");
    }
}
