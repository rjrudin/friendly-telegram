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

    public final static String HOST = "localhost";
    public final static int PORT = 8003;
    public final static String USERNAME = "mlcp2-test-user";
    public final static String PASSWORD = "password";

    @Autowired
    protected DatabaseClientProvider databaseClientProvider;

    @Override
    protected DatabaseClient getDatabaseClient() {
        return databaseClientProvider.getDatabaseClient();
    }

    protected void configureMarkLogicConnection(CommonOptions options) {
        options.setHost(HOST);
        options.setPort(PORT);
        options.setUsername(USERNAME);
        options.setPassword(PASSWORD);
    }

    protected int getCollectionSize(String collection) {
        return Integer.parseInt(
            getDatabaseClient().newServerEval()
                .javascript(format("cts.estimate(cts.collectionQuery('%s'))", collection))
                .evalAs(String.class)
        );
    }
}
