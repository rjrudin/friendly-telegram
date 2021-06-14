package com.marklogic.mlcp2;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * It seems to make sense for a Command to be responsible for instantiating the Spring container, choosing the
 * Configuration classes that it needs.
 */
@Parameters(commandDescription = "My description goes here")
public class RunMyConfigCommand implements JobCommand {

    @Parameter(
            names = {"--input_file_path"},
            description = "TODO"
    )
    private String inputFilePath;

    @Override
    public void runJob() throws JobExecutionException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
                CommonConfig.class,
                MyConfig.class
        );

        JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
        Job job = ctx.getBean(Job.class);

        Map<String, JobParameter> jobParams = new HashMap<>();
        jobParams.put("input_file_path", new JobParameter(inputFilePath));
        JobExecution jobExecution = jobLauncher.run(job, new JobParameters(jobParams));
        System.out.println(jobExecution);
    }

    public void setInputFilePath(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }
}
