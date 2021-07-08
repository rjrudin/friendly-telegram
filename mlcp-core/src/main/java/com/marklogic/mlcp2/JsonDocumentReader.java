package com.marklogic.mlcp2;

import org.springframework.batch.item.json.JsonObjectReader;
import org.springframework.core.io.Resource;

public class JsonDocumentReader implements JsonObjectReader<JsonDocument> {

    @Override
    public JsonDocument read() throws Exception {
        return null;
    }

    @Override
    public void open(Resource resource) throws Exception {
        JsonObjectReader.super.open(resource);
    }

    @Override
    public void close() throws Exception {
        JsonObjectReader.super.close();
    }
}
