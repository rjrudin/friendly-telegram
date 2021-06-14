package com.marklogic.mlcp2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JsonDocument implements Content {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    private ObjectNode document;
    private ObjectNode metadata;

    public JsonDocument(ObjectNode document) {
        this.document = document;
        this.metadata = objectMapper.createObjectNode();
    }

    public void setUri(String uri) {
        metadata.put("uri", uri);
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(document.toString().getBytes(StandardCharsets.UTF_8));
    }

    public ObjectNode getDocument() {
        return document;
    }

    public ObjectNode getMetadata() {
        return metadata;
    }
}
