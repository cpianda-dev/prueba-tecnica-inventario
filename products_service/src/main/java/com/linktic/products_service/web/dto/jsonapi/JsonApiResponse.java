package com.linktic.products_service.web.dto.jsonapi;

public class JsonApiResponse<T> {

    private JsonApiData<T> data;
    private JsonApiLinks links;

    public JsonApiData<T> getData() {
        return data;
    }
    public void setData(JsonApiData<T> data) {
        this.data = data;
    }
    public JsonApiLinks getLinks() {
        return links;
    }
    public void setLinks(JsonApiLinks links) {
        this.links = links;
    }
}
