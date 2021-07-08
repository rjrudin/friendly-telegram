package com.marklogic.mlcp2.file;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;
import com.marklogic.mlcp2.AbstractJobRunner;
import com.marklogic.mlcp2.CommonOptions;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameter;

import java.util.HashMap;
import java.util.Map;

/**
 * It seems to make sense for a Command to be responsible for instantiating the Spring container, choosing the
 * Configuration classes that it needs.
 */
@Parameters(commandDescription = "My description goes here")
public class IngestFilesJobRunner extends AbstractJobRunner {

    @ParametersDelegate
    private CommonOptions commonOptions = new CommonOptions();

    @Parameter(
        names = {"--input_file_path"},
        description = "TODO"
    )
    private String inputFilePath;

    @Override
    public JobExecution runJob() throws JobExecutionException {
        addCommonEnvironmentProperties(commonOptions);

        Map<String, JobParameter> jobParams = new HashMap<>();
        jobParams.put("input_file_path", new JobParameter(inputFilePath));
        return runJobWithParameters(jobParams, IngestFilesConfig.class);
    }

    public CommonOptions getCommonOptions() {
        return commonOptions;
    }

    public void setCommonOptions(CommonOptions commonOptions) {
        this.commonOptions = commonOptions;
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    public void setInputFilePath(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }
}
