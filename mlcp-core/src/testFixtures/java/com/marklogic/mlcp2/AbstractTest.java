package com.marklogic.mlcp2;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.ext.helper.DatabaseClientProvider;
import com.marklogic.junit5.AbstractMarkLogicTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MlcpTestConfig.class})
public abstract class AbstractTest extends AbstractMarkLogicTest {

    @Autowired
    protected DatabaseClientProvider databaseClientProvider;

    @Override
    protected DatabaseClient getDatabaseClient() {
        return databaseClientProvider.getDatabaseClient();
    }

    protected void configureMarkLogicConnection(CommonOptions options) {
        options.setHost("localhost");
        options.setPort(8003);
        options.setUsername("admin");
        options.setPassword("admin");
    }
}
