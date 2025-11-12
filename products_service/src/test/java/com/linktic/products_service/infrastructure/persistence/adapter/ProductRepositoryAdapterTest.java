package com.linktic.products_service.infrastructure.persistence.adapter;

import com.linktic.products_service.domain.model.Product;
import com.linktic.products_service.infrastructure.mapper.ProductMapper;
import com.linktic.products_service.infrastructure.persistence.entity.ProductEntity;
import com.linktic.products_service.infrastructure.persistence.jpa.ProductJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductRepositoryAdapterTest {

    private ProductJpaRepository jpa;
    private ProductMapper mapper;
    private ProductRepositoryAdapter repository;

    @BeforeEach
    void setUp() {
        jpa = mock(ProductJpaRepository.class);
        mapper = mock(ProductMapper.class);
        repository = new ProductRepositoryAdapter(jpa, mapper);
    }

    @Test
    void save_shouldCallJpaAndMapper() {
        Product domain = new Product(null, "A", BigDecimal.TEN, LocalDateTime.now(), null);
        ProductEntity entity = new ProductEntity(null, "A", BigDecimal.TEN, LocalDateTime.now(), null);
        ProductEntity savedEntity = new ProductEntity(1L, "A", BigDecimal.TEN, LocalDateTime.now(), null);
        Product savedDomain = new Product(1L, "A", BigDecimal.TEN, LocalDateTime.now(), null);

        when(mapper.toEntity(domain)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(savedDomain);

        Product result = repository.save(domain);

        assertThat(result.getId()).isEqualTo(1L);
        verify(jpa).save(entity);
        verify(mapper).toDomain(savedEntity);
    }

    @Test
    void findById_shouldReturnDomain() {
        ProductEntity entity = new ProductEntity(1L, "A", BigDecimal.TEN, LocalDateTime.now(), null);
        Product domain = new Product(1L, "A", BigDecimal.TEN, LocalDateTime.now(), null);

        when(jpa.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<Product> result = repository.findById(1L);

        assertThat(result).isPresent().contains(domain);
    }

    @Test
    void findById_shouldReturnEmpty() {
        when(jpa.findById(1L)).thenReturn(Optional.empty());
        Optional<Product> result = repository.findById(1L);
        assertThat(result).isEmpty();
    }

    @Test
    void deleteById_shouldCallJpa() {
        doNothing().when(jpa).deleteById(1L);
        repository.deleteById(1L);
        verify(jpa).deleteById(1L);
    }

    @Test
    void findAll_shouldReturnMappedList() {
        ProductEntity e1 = new ProductEntity(1L, "A", BigDecimal.TEN, LocalDateTime.now(), null);
        ProductEntity e2 = new ProductEntity(2L, "B", BigDecimal.ONE, LocalDateTime.now(), null);
        Product d1 = new Product(1L, "A", BigDecimal.TEN, LocalDateTime.now(), null);
        Product d2 = new Product(2L, "B", BigDecimal.ONE, LocalDateTime.now(), null);

        when(jpa.findAll()).thenReturn(List.of(e1, e2));
        when(mapper.toDomain(e1)).thenReturn(d1);
        when(mapper.toDomain(e2)).thenReturn(d2);

        List<Product> result = repository.findAll();

        assertThat(result).containsExactly(d1, d2);
    }

    @Test
    void findAllPaginatedList_shouldReturnMappedPage() {
        ProductEntity e1 = new ProductEntity(1L, "A", BigDecimal.TEN, LocalDateTime.now(), null);
        ProductEntity e2 = new ProductEntity(2L, "B", BigDecimal.ONE, LocalDateTime.now(), null);
        Product d1 = new Product(1L, "A", BigDecimal.TEN, LocalDateTime.now(), null);
        Product d2 = new Product(2L, "B", BigDecimal.ONE, LocalDateTime.now(), null);

        Pageable pageable = PageRequest.of(0, 2);
        Page<ProductEntity> page = new PageImpl<>(List.of(e1, e2));
        when(jpa.findAll(pageable)).thenReturn(page);
        when(mapper.toDomain(e1)).thenReturn(d1);
        when(mapper.toDomain(e2)).thenReturn(d2);

        Page<Product> result = repository.findAllPaginatedList(pageable);

        assertThat(result.getContent()).containsExactly(d1, d2);
    }
}
