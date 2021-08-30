package com.marklogic.mlcp2.jdbc;

import com.marklogic.mlcp2.AbstractTest;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IngestRowsNoCliTest extends AbstractTest {

    @BeforeEach
    void beforeEach() throws Exception {
        // Setup a sample h2 database for this test
        RunScript.main(
            "-url", "jdbc:h2:file:./build/h2-sample-db",
            "-user", "sa",
            "-script", "data/setup-h2sample-db.sql"
        );
    }

    @Test
    void test() throws Exception {
        IngestRowsJobRunner runner = new IngestRowsJobRunner();
        configureMarkLogicConnection(runner.getCommonOptions());

        runner.setJdbcDriver("org.h2.Driver");
        runner.setJdbcUrl("jdbc:h2:file:./build/h2-sample-db");
        runner.setJdbcUsername("sa");
        runner.setJdbcPassword("");
        runner.setSql("SELECT * FROM ITEM");

        JobExecution jobExecution = runner.runJob();
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

        String count = getDatabaseClient().newServerEval().javascript("cts.estimate(cts.collectionQuery('mlcp-data'))").evalAs(String.class);
        assertEquals(650, Integer.parseInt(count), "Expected 650 docs, as there are 650 rows in the ITEM table");
    }
}
