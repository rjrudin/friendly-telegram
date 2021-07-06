package com.marklogic.mlcp2.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.marklogic.mlcp2.CommonConfig;
import com.marklogic.mlcp2.file.IngestFilesConfig;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameter;

import java.util.HashMap;
import java.util.Map;

/**
 * It seems to make sense for a Command to be responsible for instantiating the Spring container, choosing the
 * Configuration classes that it needs.
 */
@Parameters(commandDescription = "My description goes here")
public class IngestFilesCommand extends CommandSupport {

    @Parameter(
        names = {"--input_file_path"},
        description = "TODO"
    )
    private String inputFilePath;

    @Override
    public void runJob() throws JobExecutionException {
        Map<String, JobParameter> jobParams = new HashMap<>();
        jobParams.put("input_file_path", new JobParameter(inputFilePath));
        runJobWithParameters(jobParams, CommonConfig.class, IngestFilesConfig.class);
    }

    public void setInputFilePath(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }
}
