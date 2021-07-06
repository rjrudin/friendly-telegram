package com.marklogic.mlcp2.cli;

import com.beust.jcommander.DynamicParameter;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameter;

import java.util.HashMap;
import java.util.Map;

@Parameters(commandDescription = "Allows for a custom Configuration class to be used")
public class CustomCommand extends CommandSupport {

    @Parameter(
        names = {"--class_name"},
        description = "Fully-qualified class name of the Spring Batch Configuration class to use for defining a job to run"
    )
    private String className;

    @DynamicParameter(names = "-D", description = "Dynamic parameters are passed as job parameters to the Spring Batch job")
    private Map<String, String> dynamicParams = new HashMap<>();

    @Override
    public void runJob() throws JobExecutionException {
        Class<?> customClass;
        try {
            logger.info("Finding class definition for class name: " + className);
            customClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unable to find class definition for class name: " + className, e);
        }

        Map<String, JobParameter> jobParams = new HashMap<>();
        if (dynamicParams != null) {
            dynamicParams.forEach((name, value) -> jobParams.put(name, new JobParameter(value)));
        }

        runJobWithParameters(jobParams, customClass);
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setDynamicParams(Map<String, String> dynamicParams) {
        this.dynamicParams = dynamicParams;
    }
}
