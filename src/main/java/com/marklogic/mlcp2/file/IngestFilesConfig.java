package com.marklogic.mlcp2.file;

import com.marklogic.mlcp2.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

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

        FlatFileItemReader<JsonDocument> flatFileReader = new FlatFileItemReaderBuilder<JsonDocument>()
            //.resource(new FileSystemResource(inputFilePath))
            .name("csvReader")
            .linesToSkip(1)
            .skippedLinesCallback(line -> lineTokenizer.setNames(line.split(delimiter)))
            .lineMapper(lineMapper)
            .build();

        // It seems we'd always do this, right? It works for a single file, and it works for a directory.
        Resource[] resources;
        try {
            resources = new PathMatchingResourcePatternResolver().getResources("file:" + inputFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MultiResourceItemReader<JsonDocument> reader = new MultiResourceItemReaderBuilder<JsonDocument>()
            .delegate(flatFileReader)
            .resources(resources)
            .name("multiReader")
            .build();

        return stepBuilderFactory.get("step1")
            .<JsonDocument, Content>chunk(100)
            .reader(reader)
            .writer(bulkContentItemWriter())
            .listener(new LoggingStepExecutionListener())
            .build();
    }

    @Bean
    public BulkContentItemWriter bulkContentItemWriter() {
        return new BulkContentItemWriter();
    }
}
