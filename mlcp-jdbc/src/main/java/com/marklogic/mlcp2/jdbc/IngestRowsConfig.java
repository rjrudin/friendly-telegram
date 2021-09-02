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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.util.Map;

@Configuration
@Import(BaseConfig.class)
public class IngestRowsConfig extends LoggingObject {

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
    ) throws Exception {
        Assert.hasText(sql, "Job parameter 'sql' must be defined");

        // Uses Spring Batch's JdbcCursorItemReader and Spring JDBC's ColumnMapRowMapper to map each row
        // to a Map<String, Object>. Normally, if you want more control, standard practice is to bind column values to
        // a POJO and perform any validation/transformation/etc you need to on that object.
        JdbcCursorItemReader<Map<String, Object>> jdbcReader = new JdbcCursorItemReader();
        jdbcReader.setDataSource(newDataSource());
        jdbcReader.setRowMapper(new ColumnMapRowMapper());
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

    /**
     * Supports dynamically constructing a JDBC DataSource via a user-specified file path.
     */
    private DataSource newDataSource() throws Exception {
        final String jdbcDriverPath = environment.getProperty("jdbc_driver_path");
        final String jdbcDriver = environment.getProperty("jdbc_driver");
        final String jdbcUrl = environment.getRequiredProperty("jdbc_url");

        // TODO This should really use a modern pooling DataSource like HikariCP. At least for H2 though, which is used
        // for testing, it doesn't appear to be possibly to dynamically load the H2 DataSource class via
        // HikariDataSource as the latter doesn't accept a custom class loader. It instead requires a DataSource to be
        // provided to it, which could be challenging to support via simple properties.
        SimpleDriverDataSource dataSource;

        if (StringUtils.hasText(jdbcDriverPath)) {
            // TODO Assuming a single file, likely need to support a directory
            ClassLoader loader = URLClassLoader.newInstance(new URL[]{new File(jdbcDriverPath).toURI().toURL()});
            Class<?> clazz = loader.loadClass(jdbcDriver);
            Driver driver = (Driver) clazz.newInstance();
            dataSource = new SimpleDriverDataSource(driver, jdbcUrl);
        } else {
            dataSource = new SimpleDriverDataSource();
            dataSource.setUrl(jdbcUrl);
            dataSource.setDriverClass((Class<? extends Driver>) Class.forName(jdbcDriver));
        }

        dataSource.setUsername(environment.getProperty("jdbc_username"));
        dataSource.setPassword(environment.getProperty("jdbc_password"));
        return dataSource;
    }
}
