package com.marklogic.mlcp2.aws;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IngestS3Config {

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   @Qualifier("clearDatabaseStep") Step step,
                   @Qualifier("myStep") Step myStep) {
        return jobBuilderFactory.get("mlcpJob")
            .start(step)
            .next(myStep)
            .build();
    }
    
}
