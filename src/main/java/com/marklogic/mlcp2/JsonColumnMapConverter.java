package com.marklogic.mlcp2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.batch.item.ItemProcessor;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonColumnMapConverter implements ItemProcessor<Map<String, Object>, Content> {

    private ObjectMapper objectMapper;

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("hello", "world");
        map.put("aNumber", 123);
        Map<String, Object> child = new LinkedHashMap<>();
        map.put("child", child);
        child.put("hey", "there");

        ObjectNode document = new ObjectMapper().convertValue(map, ObjectNode.class);
        System.out.println(document);
    }

    public JsonColumnMapConverter() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Content process(Map<String, Object> item) {
        return new JsonDocument(objectMapper.convertValue(item, ObjectNode.class));
    }
}
