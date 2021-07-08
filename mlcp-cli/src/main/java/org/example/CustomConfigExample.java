package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.marklogic.mlcp2.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(BaseConfig.class)
public class CustomConfigExample {

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory, Step step) {
        return jobBuilderFactory.get("myCustomJob")
            .start(step)
            .build();
    }

    @Bean
    @JobScope
    public Step myStep(
        StepBuilderFactory stepBuilderFactory,
        @Value("#{jobParameters['my_limit']}") Integer myLimit
    ) {
        return stepBuilderFactory.get("step1")
            .<JsonDocument, Content>chunk(100)
            .reader(new MyCustomReader(myLimit))
            .writer(bulkContentItemWriter())
            .listener(new LoggingStepExecutionListener())
            .build();
    }

    @Bean
    public BulkContentItemWriter bulkContentItemWriter() {
        return new BulkContentItemWriter();
    }

}

class MyCustomReader implements ItemReader<JsonDocument> {

    private final ObjectMapper objectMapper;
    private final int limit;
    private int counter = 0;

    public MyCustomReader(int limit) {
        this.limit = limit;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public JsonDocument read() {
        if (counter < limit) {
            ObjectNode content = objectMapper.createObjectNode().put("customerId", counter);
            counter++;
            return new JsonDocument(content);
        }

        return null;
    }
}
