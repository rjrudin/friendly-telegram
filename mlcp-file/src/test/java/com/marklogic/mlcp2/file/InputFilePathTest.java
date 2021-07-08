package com.marklogic.mlcp2.file;

import com.marklogic.mlcp2.AbstractTest;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;

public class InputFilePathTest extends AbstractTest {

    @Test
    void test() throws Exception {
        IngestFilesJobRunner runner = new IngestFilesJobRunner();
        configureMarkLogicConnection(runner.getCommonOptions());
        runner.setInputFilePath("data/csv/**/*.csv");

        JobExecution jobExecution = runner.runJob();
        logger.info(jobExecution.toString());
    }
}
