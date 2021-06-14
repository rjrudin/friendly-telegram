package com.marklogic.mlcp2;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.mgmt.ManageClient;
import com.marklogic.mgmt.ManageConfig;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@EnableBatchProcessing
public class MyConfig {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyConfig.class);
        JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
        Job job = ctx.getBean(Job.class);

        Map<String, JobParameter> jobParams = new HashMap<>();
        jobParams.put("host", new JobParameter("somehost"));
        JobExecution jobExecution = jobLauncher.run(job, new JobParameters(jobParams));
        System.out.println(jobExecution);
    }

//    @Value("file:data/json/*.json")
//    private Resource[] inputResources;

    @Bean
    public ManageClient manageClient() {
        return new ManageClient(new ManageConfig("localhost", 8002, "admin", "admin"));
    }

    /**
     * Defines the job for Spring Batch to run. This job consists of a single step, defined below.
     */
    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   @Qualifier("clearDatabaseStep") Step step,
                   @Qualifier("myStep") Step myStep) {
        return jobBuilderFactory.get("mlcpJob")
                .start(step)
                .next(myStep)
                .build();
    }

    @Bean
    public Step clearDatabaseStep(StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("clearDatabaseStep")
                .tasklet(new ClearDatabaseTasklet(manageClient()))
                .build();
    }

    /**
     * Defines the single step in the job, along with all of the job parameters for the migration process.
     */
    @Bean
    @JobScope
    public Step myStep(StepBuilderFactory stepBuilderFactory,
                       @Value("#{jobParameters['host']}") String host) {

        final String delimiter = ",";

        DefaultLineMapper lineMapper = new DefaultLineMapper();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer(delimiter);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new JsonFieldSetMapper());

        FlatFileItemReader<JsonDocument> reader = new FlatFileItemReaderBuilder<ObjectNode>()
                .resource(new FileSystemResource("data/csv/customers1.csv"))
                .name("csvReader")
                .linesToSkip(1)
                .skippedLinesCallback(line -> lineTokenizer.setNames(line.split(delimiter)))
                .lineMapper(lineMapper)
                .build();

        DatabaseClient client = DatabaseClientFactory.newClient("localhost", 8003,
                new DatabaseClientFactory.DigestAuthContext("admin", "admin"));
        BulkContentItemWriter writer = new BulkContentItemWriter(Arrays.asList(client));

        return stepBuilderFactory.get("step1")
                .<JsonDocument, Content>chunk(1)
                .reader(reader)
//                .processor(processor)
                .writer(writer)
                .build();
    }
}
