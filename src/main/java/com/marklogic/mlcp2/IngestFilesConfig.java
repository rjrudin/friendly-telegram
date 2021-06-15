package com.marklogic.mlcp2;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class IngestFilesConfig {

//    @Value("file:data/json/*.json")
//    private Resource[] inputResources;

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
                .tasklet(clearDatabaseTasklet())
                .build();
    }

    @Bean
    public ClearDatabaseTasklet clearDatabaseTasklet() {
        return new ClearDatabaseTasklet();
    }

    @Bean
    @JobScope
    public Step myStep(
            StepBuilderFactory stepBuilderFactory,
            @Value("#{jobParameters['input_file_path']}") String inputFilePath
    ) {
        final String delimiter = ",";

        DefaultLineMapper lineMapper = new DefaultLineMapper();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer(delimiter);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new JsonFieldSetMapper());

        FlatFileItemReader<JsonDocument> reader = new FlatFileItemReaderBuilder<ObjectNode>()
                .resource(new FileSystemResource(inputFilePath))
                .name("csvReader")
                .linesToSkip(1)
                .skippedLinesCallback(line -> lineTokenizer.setNames(line.split(delimiter)))
                .lineMapper(lineMapper)
                .build();

        return stepBuilderFactory.get("step1")
                .<JsonDocument, Content>chunk(100)
                .reader(reader)
                .writer(bulkContentItemWriter())
                .build();
    }

    @Bean
    public BulkContentItemWriter bulkContentItemWriter() {
        return new BulkContentItemWriter();
    }
}
