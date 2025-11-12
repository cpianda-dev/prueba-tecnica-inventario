package com.linktic.products_service.web.dto.jsonapi;

import java.util.List;

public class JsonApiListResponse<T> {

    private List<JsonApiData<T>> data;
    private JsonApiLinks links;
    private JsonApiMeta meta;

    public List<JsonApiData<T>> getData() {
        return data;
    }
    public void setData(List<JsonApiData<T>> data) {
        this.data = data;
    }
    public JsonApiLinks getLinks() {
        return links;
    }
    public void setLinks(JsonApiLinks links) {
        this.links = links;
    }
    public JsonApiMeta getMeta() {
        return meta;
    }
    public void setMeta(JsonApiMeta meta) {
        this.meta = meta;
    }
}
