package com.linktic.products_service.web.dto.jsonapi;

import jakarta.validation.constraints.NotNull;

public class JsonApiRequest<T> {
    @NotNull
    private JsonApiData<T> data;

    public JsonApiData<T> getData() {
        return data;
    }
    public void setData(JsonApiData<T> data) {
        this.data = data;
    }
}
