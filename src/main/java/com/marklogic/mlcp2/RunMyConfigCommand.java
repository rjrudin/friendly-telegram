package com.marklogic.mlcp2;

import com.beust.jcommander.Parameters;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * It seems to make sense for a Command to be responsible for instantiating the Spring container, choosing the
 * Configuration classes that it needs.
 */
@Parameters(commandDescription = "My description goes here")
public class RunMyConfigCommand implements MlcpCommand {

    @Override
    public void run() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
                CommonConfig.class,
                MyConfig.class
        );

        JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
        Job job = ctx.getBean(Job.class);

        Map<String, JobParameter> jobParams = new HashMap<>();
        jobParams.put("+host", new JobParameter("somehost"));
        jobParams.put("-hidden", new JobParameter("somehost"));
        JobExecution jobExecution;
        try {
            jobExecution = jobLauncher.run(job, new JobParameters(jobParams));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        System.out.println(jobExecution);
    }
}
