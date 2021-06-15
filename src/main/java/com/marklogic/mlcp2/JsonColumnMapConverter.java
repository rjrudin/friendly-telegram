package com.marklogic.mlcp2;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.batch.item.ItemProcessor;

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
