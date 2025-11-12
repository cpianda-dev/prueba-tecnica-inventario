package com.linktic.products_service.domain.repository;

import com.linktic.products_service.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
    void deleteById(Long id);
    List<Product> findAll();
    Page<Product> findAllPaginatedList(Pageable pageable);
}
