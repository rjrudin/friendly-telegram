package com.marklogic.mlcp2;

import com.beust.jcommander.DynamicParameter;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameter;

import java.util.HashMap;
import java.util.Map;

@Parameters(commandDescription = "Allows for a custom Configuration class to be used")
public class CustomJobRunner extends AbstractJobRunner {

    @ParametersDelegate
    CommonOptions commonOptions = new CommonOptions();

    @Parameter(
        names = {"--class_name"},
        description = "Fully-qualified class name of the Spring Batch Configuration class to use for defining a job to run"
    )
    String className;

    // TODO Distinguish between environment props and job parameters
    @DynamicParameter(names = "-D", description = "Dynamic parameters are passed as job parameters to the Spring Batch job")
    Map<String, String> dynamicParams = new HashMap<>();

    @Override
    public JobExecution runJob() throws JobExecutionException {
        Class<?> customClass;
        try {
            logger.info("Finding class definition for class name: " + className);
            customClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unable to find class definition for class name: " + className, e);
        }

        addCommonEnvironmentProperties(commonOptions);

        Map<String, JobParameter> jobParams = new HashMap<>();
        if (dynamicParams != null) {
            dynamicParams.forEach((name, value) -> jobParams.put(name, new JobParameter(value)));
        }

        return runJobWithParameters(jobParams, customClass);
    }
}
