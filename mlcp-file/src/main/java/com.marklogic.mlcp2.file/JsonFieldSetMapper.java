package com.marklogic.mlcp2.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.marklogic.mlcp2.JsonDocument;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class JsonFieldSetMapper implements FieldSetMapper<JsonDocument> {

    private ObjectMapper objectMapper;

    public JsonFieldSetMapper() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public JsonDocument mapFieldSet(FieldSet fieldSet) {
        int count = fieldSet.getFieldCount();
        ObjectNode node = objectMapper.createObjectNode();
        for (int i = 0; i < count; i++) {
            node.put(fieldSet.getNames()[i], fieldSet.getValues()[i]);
        }
        return new JsonDocument(node);
    }
}
