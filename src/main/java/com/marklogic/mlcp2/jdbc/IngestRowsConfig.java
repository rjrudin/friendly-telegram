package com.marklogic.mlcp2.jdbc;

import com.marklogic.mlcp2.BulkContentItemWriter;
import com.marklogic.mlcp2.ClearDatabaseTasklet;
import com.marklogic.mlcp2.Content;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.Map;

@Configuration
public class IngestRowsConfig {

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   @Qualifier("clearDatabaseStep") Step step,
                   @Qualifier("ingestRowsStep") Step myStep) {
        return jobBuilderFactory.get("ingestRowsJob")
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
    public Step ingestRowsStep(
        StepBuilderFactory stepBuilderFactory,
        @Value("#{jobParameters['jdbc_url']}") String jdbcUrl
    ) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        // Uses Spring Batch's JdbcCursorItemReader and Spring JDBC's ColumnMapRowMapper to map each row
        // to a Map<String, Object>. Normally, if you want more control, standard practice is to bind column values to
        // a POJO and perform any validation/transformation/etc you need to on that object.
        JdbcCursorItemReader<Map<String, Object>> r = new JdbcCursorItemReader();
        r.setRowMapper(new ColumnMapRowMapper());
        r.setDataSource(dataSource);
        r.setSql("SELECT * FROM Item");
        r.setRowMapper(new ColumnMapRowMapper());
        ItemReader<Map<String, Object>> reader = r;

        return stepBuilderFactory.get("ingestRowsStep")
            .<Map<String, Object>, Content>chunk(100)
            .reader(reader)
            .processor(new JsonColumnMapConverter())
            .writer(bulkContentItemWriter())
            .listener(new StepExecutionListener() {
                @Override
                public void beforeStep(StepExecution stepExecution) {
                    System.out.println("Before step! " + stepExecution);
                }

                @Override
                public ExitStatus afterStep(StepExecution stepExecution) {
                    System.out.println("After step! " + stepExecution);
                    return ExitStatus.COMPLETED;
                }
            })
            .build();
    }

    @Bean
    public BulkContentItemWriter bulkContentItemWriter() {
        return new BulkContentItemWriter();
    }

}
