package com.marklogic.mlcp2.jdbc;

import com.marklogic.mlcp2.jdbc.IngestRowsJobRunner;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;

public class IngestRowsNoCliTest { // extends AbstractTest {

    //            Main.main(new String[]{
//        "ingestRows",
//            "--host", "localhost",
//            "--port", "8003",
//            "--username", "admin",
//            "--password", "admin",
//            "--batch_size", "50",
//            "--jdbc_driver", "org.h2.Driver",
//            "--jdbc_url", "jdbc:h2:file:./data/h2/sample",
//            "--jdbc_username", "sa",
//            "--jdbc_password", "",
//            "--sql", "SELECT * FROM Customer"
//    });
//

    /**
     * So we want to reuse the Config class, and we'll need to push in job params and environment properties.
     * So we'll just demonstrate everything without the CLI being used here.
     */
    @Test
    void test() throws Exception {
        IngestRowsJobRunner runner = new IngestRowsJobRunner();
        runner.getCommonOptions().setHost("localhost");
        runner.getCommonOptions().setPort(8003);
        runner.getCommonOptions().setUsername("admin");
        runner.getCommonOptions().setPassword("admin");

        runner.setJdbcDriver("org.h2.Driver");
        runner.setJdbcUrl("jdbc:h2:file:./data/h2/sample");
        runner.setJdbcUsername("sa");
        runner.setJdbcPassword("");
        runner.setSql("SELECT * FROM ITEM");

        JobExecution jobExecution = runner.runJob();
//        logger.info(jobExecution.toString());
    }
}
