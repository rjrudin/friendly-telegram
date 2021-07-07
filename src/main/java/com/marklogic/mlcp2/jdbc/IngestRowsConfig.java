package com.marklogic.mlcp2.jdbc;

import com.marklogic.mlcp2.BulkContentItemWriter;
import com.marklogic.mlcp2.Content;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.Map;

@Configuration
public class IngestRowsConfig {

    @Autowired
    Environment environment;

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory, Step myStep) {
        return jobBuilderFactory.get("ingestRowsJob").start(myStep).build();
    }

    @Bean
    @JobScope
    public Step ingestRowsStep(
        StepBuilderFactory stepBuilderFactory,
        @Value("#{jobParameters['batch_size']}") Integer batchSize,
        @Value("#{jobParameters['sql']}") String sql
    ) {
        // TODO Use a connection pool here
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("jdbcDriver"));
        dataSource.setUrl(environment.getProperty("jdbcUrl"));
        dataSource.setUsername(environment.getProperty("jdbcUsername"));
        dataSource.setPassword(environment.getProperty("jdbcPassword"));

        // Uses Spring Batch's JdbcCursorItemReader and Spring JDBC's ColumnMapRowMapper to map each row
        // to a Map<String, Object>. Normally, if you want more control, standard practice is to bind column values to
        // a POJO and perform any validation/transformation/etc you need to on that object.
        JdbcCursorItemReader<Map<String, Object>> jdbcReader = new JdbcCursorItemReader();
        jdbcReader.setRowMapper(new ColumnMapRowMapper());
        jdbcReader.setDataSource(dataSource);
        jdbcReader.setSql(sql);
        jdbcReader.setRowMapper(new ColumnMapRowMapper());
        ItemReader<Map<String, Object>> reader = jdbcReader;

        System.out.println("BTCH: " + batchSize);

        return stepBuilderFactory.get("ingestRowsStep")
            .<Map<String, Object>, Content>chunk(batchSize)
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
