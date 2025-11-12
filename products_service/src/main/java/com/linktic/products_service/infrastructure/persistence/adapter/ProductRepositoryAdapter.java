package com.linktic.products_service.infrastructure.persistence.adapter;

import com.linktic.products_service.domain.model.Product;
import com.linktic.products_service.domain.repository.ProductRepository;
import com.linktic.products_service.infrastructure.mapper.ProductMapper;
import com.linktic.products_service.infrastructure.persistence.entity.ProductEntity;
import com.linktic.products_service.infrastructure.persistence.jpa.ProductJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryAdapter implements ProductRepository {
    private final ProductJpaRepository jpa;
    private final ProductMapper mapper;

    public ProductRepositoryAdapter(ProductJpaRepository jpa, ProductMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Product save(Product product) {
        ProductEntity saved = jpa.save(mapper.toEntity(product));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpa.deleteById(id);
    }

    @Override
    public List<Product> findAll() {
        return jpa.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Page<Product> findAllPaginatedList(Pageable pageable) {
        return jpa.findAll(pageable).map(mapper::toDomain);
    }

}
