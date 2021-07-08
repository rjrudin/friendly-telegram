package com.marklogic.mlcp2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.dataservices.IOEndpoint;
import com.marklogic.client.dataservices.InputCaller;
import com.marklogic.client.ext.DatabaseClientConfig;
import com.marklogic.client.ext.DefaultConfiguredDatabaseClientFactory;
import com.marklogic.client.io.InputStreamHandle;
import com.marklogic.client.io.JacksonHandle;
import com.marklogic.client.io.StringHandle;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamSupport;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Assuming we want to use the Bulk API...
 * <p>
 * I think we'd want a List of clients passed in here. This class will then setup the Bulk callers that it needs.
 * It will also need access to config parameters for Bulk callers, such as thread count, along with what DS module
 * to use.
 * <p>
 * I think installation of the default DS modules would happen in the "open" method, right? We'd need config params
 * then on permissions. And if the user has provided custom DS modules, we don't need to do anything.
 * <p>
 * For "close", I think that's where we'd wait for things to finish.
 * <p>
 * Now, this could handle all 4 types of docs - binary, json, text, or xml. But it would need a separate BulkInputCaller
 * for each, since a BIC is specific to one document type. This could then support a more generic interface that returns
 * Content and Metadata. But hopefully the Java Client supports "anyDocument" so we can e.g. send a JSON metadata object
 * and any type for content. Then we only need one BIC endpoint.
 * <p>
 * Let's talk about reusing this for a File vs a row from a CSV. In the former case, we can use a FileHandle - actually
 * never mind. A FileHandle still reads all of its contents into memory as a byte[] array. Well, at least that doesn't
 * improve performance by allowing for streaming from disk to the HTTP request. Hmm.
 * <p>
 * Anyway - let's for a file, we send along a FileHandle. While for a JSON object from a CSV, we send along a JacksonHandle.
 * It appears that as long as the contents become a JSON document, then we're good to go, right?
 */
public class BulkContentItemWriter extends ItemStreamSupport implements ItemStreamWriter<Content> {

    // TODO We'll want to support multiple hosts, perhaps dynamically via the Manage API?
    @Autowired
    private DatabaseClientConfig databaseClientConfig;

    private int threadCount = 24;
    private List<InputCaller.BulkInputCaller<InputStream>> bulkInputCallers;
    private int callerCounter = 0;

    @Override
    public void open(ExecutionContext executionContext) {
        ObjectMapper objectMapper = new ObjectMapper();
        final StringHandle apiHandle = new StringHandle(BULK_API);
        final JacksonHandle endpointConstants = new JacksonHandle(objectMapper.createObjectNode().put("simpleBulkService", true));

        bulkInputCallers = new ArrayList<>();

        DatabaseClient databaseClient = new DefaultConfiguredDatabaseClientFactory().newDatabaseClient(databaseClientConfig);

        List<DatabaseClient> databaseClients = Arrays.asList(databaseClient);
        databaseClients.forEach(client -> {
            InputCaller<InputStream> inputCaller;
            try {
                inputCaller = InputCaller.on(client, apiHandle, new InputStreamHandle());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            IOEndpoint.CallContext[] callContexts = new IOEndpoint.CallContext[threadCount];
            for (int i = 0; i < threadCount; i++) {
                callContexts[i] = inputCaller.newCallContext().withEndpointConstants(endpointConstants);
            }
            bulkInputCallers.add(inputCaller.bulkCaller(callContexts, threadCount));
        });
    }

    @Override
    public void write(List<? extends Content> items) {
        InputCaller.BulkInputCaller caller = bulkInputCallers.get(callerCounter);
        callerCounter++;
        if (callerCounter >= bulkInputCallers.size()) {
            callerCounter = 0;
        }
        items.forEach(item -> caller.accept(item.getInputStream()));
    }

    @Override
    public void close() {
        this.bulkInputCallers.forEach(caller -> caller.awaitCompletion());
    }

    private static final String BULK_API = "{\n" +
            "  \"endpoint\": \"/writeDocuments.sjs\",\n" +
            "  \"params\": [\n" +
            "    {\n" +
            "      \"name\": \"endpointConstants\",\n" +
            "      \"datatype\": \"jsonDocument\",\n" +
            "      \"multiple\": false,\n" +
            "      \"nullable\": true\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"input\",\n" +
            "      \"datatype\": \"jsonDocument\",\n" +
            "      \"multiple\": true,\n" +
            "      \"nullable\": true\n" +
            "    }\n" +
            "  ],\n" +
            "  \"$bulk\": {\n" +
            "    \"inputBatchSize\": 100\n" +
            "  }\n" +
            "}";
}
