package com.marklogic.mlcp2.cli;

import com.marklogic.client.ext.helper.LoggingObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

@Configuration
public class CliConfig extends LoggingObject {

    @Autowired
    ConfigurableEnvironment configurableEnvironment;

    @Bean
    @Lazy(false)
    public PropertiesPropertySource cliProperties() {
        PropertiesPropertySource source = new PropertiesPropertySource("cliProperties", CommonOptions.getInstance().toProperties());
        configurableEnvironment.getPropertySources().addFirst(source);
        return source;
    }

}
