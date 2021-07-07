package com.marklogic.mlcp2.cli;

import com.beust.jcommander.DynamicParameter;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameter;

import java.util.HashMap;
import java.util.Map;

@Parameters(commandDescription = "Allows for a custom Configuration class to be used")
public class CustomCommand extends CommandSupport {

    @ParametersDelegate
    CommonOptions commonOptions = new CommonOptions();

    @Parameter(
        names = {"--class_name"},
        description = "Fully-qualified class name of the Spring Batch Configuration class to use for defining a job to run"
    )
    String className;

    // We may need two sets of dynamic parameters here; one for environment properties, and one for job parameters.
    // That allows a custom command user to e.g. include a password as a dynamic param and not have it show up in
    // the list of job parameters.
    @DynamicParameter(names = "-D", description = "Dynamic parameters are passed as job parameters to the Spring Batch job")
    Map<String, String> dynamicParams = new HashMap<>();

    @Override
    public void runJob() throws JobExecutionException {
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

        runJobWithParameters(jobParams, customClass);
    }
}
