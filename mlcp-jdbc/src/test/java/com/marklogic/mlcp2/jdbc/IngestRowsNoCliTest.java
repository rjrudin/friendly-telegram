package com.marklogic.mlcp2.jdbc;

import com.marklogic.mlcp2.AbstractTest;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;

public class IngestRowsNoCliTest extends AbstractTest {

    @Test
    void test() throws Exception {
        IngestRowsJobRunner runner = new IngestRowsJobRunner();
        runner.getCommonOptions().setHost("localhost");
        runner.getCommonOptions().setPort(8003);
        runner.getCommonOptions().setUsername("admin");
        runner.getCommonOptions().setPassword("admin");

        runner.setJdbcDriver("org.h2.Driver");
        runner.setJdbcUrl("jdbc:h2:file:../data/h2/sample");
        runner.setJdbcUsername("sa");
        runner.setJdbcPassword("");
        runner.setSql("SELECT * FROM ITEM");

        JobExecution jobExecution = runner.runJob();
        logger.info(jobExecution.toString());
    }
}
