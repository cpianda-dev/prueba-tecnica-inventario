package com.linktic.products_service.domain.service;

import com.linktic.products_service.domain.model.Product;
import com.linktic.products_service.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    private ProductRepository repository;
    private ProductService service;

    @BeforeEach
    void setUp() {
        repository = mock(ProductRepository.class);
        service = new ProductService(repository);
    }

    @Test
    void create_shouldSaveProduct_whenPriceValid() {
        Product product = new Product(null, "Test", BigDecimal.TEN, LocalDateTime.now(), null);
        when(repository.save(any(Product.class))).thenReturn(product);

        Product created = service.create("Test", BigDecimal.TEN);

        assertNotNull(created);
        assertEquals("Test", created.getName());
        assertEquals(BigDecimal.TEN, created.getPrice());
        verify(repository, times(1)).save(any(Product.class));
    }

    @Test
    void create_shouldThrowException_whenPriceInvalid() {
        assertThrows(IllegalArgumentException.class, () -> service.create("Test", BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> service.create("Test", null));
        verify(repository, never()).save(any());
    }

    @Test
    void get_shouldReturnProduct_whenExists() {
        Product product = new Product(1L, "Test", BigDecimal.TEN, LocalDateTime.now(), null);
        when(repository.findById(1L)).thenReturn(Optional.of(product));

        Product result = service.get(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void get_shouldThrowException_whenNotExists() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.get(1L));
    }

    @Test
    void update_shouldModifyProduct_whenValid() {
        Product product = new Product(1L, "Old", BigDecimal.ONE, LocalDateTime.now(), null);
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(repository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        Product updated = service.update(1L, "New", BigDecimal.TEN);

        assertEquals("New", updated.getName());
        assertEquals(BigDecimal.TEN, updated.getPrice());
        assertNotNull(updated.getUpdatedAt());
        verify(repository).save(product);
    }

    @Test
    void update_shouldThrowException_whenPriceInvalid() {
        Product product = new Product(1L, "Old", BigDecimal.ONE, LocalDateTime.now(), null);
        when(repository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(IllegalArgumentException.class, () -> service.update(1L, "New", BigDecimal.ZERO));
    }

    @Test
    void delete_shouldCallRepository() {
        doNothing().when(repository).deleteById(1L);
        service.delete(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void list_shouldReturnAllProducts() {
        List<Product> products = Arrays.asList(
                new Product(1L, "A", BigDecimal.ONE, LocalDateTime.now(), null),
                new Product(2L, "B", BigDecimal.TEN, LocalDateTime.now(), null)
        );
        when(repository.findAll()).thenReturn(products);

        List<Product> result = service.list();

        assertThat(result).hasSize(2).extracting(Product::getName).contains("A", "B");
    }

    @Test
    void paginatedList_shouldReturnPage() {
        List<Product> products = Arrays.asList(
                new Product(1L, "A", BigDecimal.ONE, LocalDateTime.now(), null)
        );
        Page<Product> page = new PageImpl<>(products);
        when(repository.findAllPaginatedList(PageRequest.of(0, 10))).thenReturn(page);

        Page<Product> result = service.paginatedList(1, 10);

        assertThat(result.getContent()).hasSize(1);
        assertEquals("A", result.getContent().get(0).getName());
    }

    @Test
    void get_shouldThrowNoSuchElementException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.get(1L));
    }

    @Test
    void create_shouldThrowIllegalArgumentException_whenPriceInvalid() {
        assertThrows(IllegalArgumentException.class, () -> service.create("Test", BigDecimal.ZERO));
    }
}