package com.linktic.products_service.web.handler;

import com.linktic.products_service.web.controller.ProductController;
import com.linktic.products_service.web.dto.jsonapi.JsonApiError;
import com.linktic.products_service.web.dto.jsonapi.JsonApiRequest;
import com.linktic.products_service.web.dto.jsonapi.ProductDto;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;

import java.lang.reflect.Method;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

class RestExceptionHandlerTest {

    private final RestExceptionHandler handler = new RestExceptionHandler();

    @Test
    void handleNotFound_returns404() {
        ResponseEntity<?> resp = handler.handleNotFound(new NoSuchElementException("missing"));
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat((List<?>) resp.getBody()).isNotEmpty();
    }

    @Test
    void handleBadRequest_returns400() {
        ResponseEntity<?> resp = handler.handleBadRequest(new IllegalArgumentException("bad"));
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void handleMethodArgumentNotValid_returns400WithFirstFieldMessage() throws NoSuchMethodException {
        // Simula un error de validación en ProductDto.name
        ProductDto target = new ProductDto();
        BeanPropertyBindingResult br = new BeanPropertyBindingResult(target, "product");
        br.addError(new FieldError("product", "name", "must not be blank"));

        Method m = ProductController.class.getMethod("create", JsonApiRequest.class);
        MethodParameter mp = new MethodParameter(m, 0);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(mp, br);

        ResponseEntity<?> resp = handler.handleMethodArgumentNotValid(
                ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, new ServletWebRequest(new MockHttpServletRequest()));

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        List<?> list = (List<?>) resp.getBody();
        assertThat(list).isNotEmpty();
        Object first = list.get(0);
        assertThat(first).isInstanceOf(JsonApiError.class);
        JsonApiError err = (JsonApiError) first;
        assertThat(err.getStatus()).isEqualTo("400");
        assertThat(err.getTitle()).isEqualTo("Validation Error");
        assertThat(err.getDetail()).contains("name").contains("must not be blank");
    }
}
