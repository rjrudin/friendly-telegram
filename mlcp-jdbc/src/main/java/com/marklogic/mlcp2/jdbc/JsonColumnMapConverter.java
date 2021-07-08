package com.marklogic.mlcp2.jdbc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.marklogic.mlcp2.Content;
import com.marklogic.mlcp2.JsonDocument;
import org.springframework.batch.item.ItemProcessor;

import java.util.Map;

/**
 * Converts a column map into a JSON document.
 */
public class JsonColumnMapConverter implements ItemProcessor<Map<String, Object>, Content> {

    private ObjectMapper objectMapper;

    public JsonColumnMapConverter() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Content process(Map<String, Object> item) {
        return new JsonDocument(objectMapper.convertValue(item, ObjectNode.class));
    }
}
