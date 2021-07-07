package com.marklogic.mlcp2.jdbc;

import com.marklogic.client.ext.helper.LoggingObject;
import com.marklogic.mlcp2.BaseConfig;
import com.marklogic.mlcp2.BulkContentItemWriter;
import com.marklogic.mlcp2.Content;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.Map;

@Configuration
@Import(BaseConfig.class)
// Ensures that Value annotations can be populated based on EnvironmentPropertySource
@Lazy
public class IngestRowsConfig extends LoggingObject {

    @Value("${jdbc_driver}")
    String jdbcDriver;

    @Value("${jdbc_url}")
    String jdbcUrl;

    @Value("${jdbc_username}")
    String jdbcUsername;

    @Value("${jdbc_password}")
    String jdbcPassword;


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
        dataSource.setDriverClassName(jdbcDriver);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(jdbcUsername);
        dataSource.setPassword(jdbcPassword);

        // Uses Spring Batch's JdbcCursorItemReader and Spring JDBC's ColumnMapRowMapper to map each row
        // to a Map<String, Object>. Normally, if you want more control, standard practice is to bind column values to
        // a POJO and perform any validation/transformation/etc you need to on that object.
        JdbcCursorItemReader<Map<String, Object>> jdbcReader = new JdbcCursorItemReader();
        jdbcReader.setRowMapper(new ColumnMapRowMapper());
        jdbcReader.setDataSource(dataSource);
        jdbcReader.setSql(sql);
        ItemReader<Map<String, Object>> reader = jdbcReader;

        return stepBuilderFactory.get("ingestRowsStep")
            .<Map<String, Object>, Content>chunk(batchSize)
            .reader(reader)
            .processor(new JsonColumnMapConverter())
            .writer(bulkContentItemWriter())
            .build();
    }

    @Bean
    public BulkContentItemWriter bulkContentItemWriter() {
        return new BulkContentItemWriter();
    }

}
