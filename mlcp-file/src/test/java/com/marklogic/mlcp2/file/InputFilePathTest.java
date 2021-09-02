package com.marklogic.mlcp2.file;

import com.marklogic.mlcp2.AbstractTest;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputFilePathTest extends AbstractTest {

    @Test
    void test() throws Exception {
        IngestFilesJobRunner runner = new IngestFilesJobRunner();
        configureMarkLogicConnection(runner.getCommonOptions());
        runner.setInputFilePath("data/csv/**/*.csv");

        JobExecution jobExecution = runner.runJob();
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus(), "Expected COMPLETED: " + jobExecution);
        assertEquals(9, getCollectionSize("mlcp-data"), "Expected 9 docs, 3 for each csv in the mlcp-file/data/csv directory");

    }
}
