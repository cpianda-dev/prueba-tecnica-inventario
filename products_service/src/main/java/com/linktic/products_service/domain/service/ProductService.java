package com.linktic.products_service.domain.service;

import com.linktic.products_service.domain.model.Product;
import com.linktic.products_service.domain.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) { this.repository = repository; }

    public Product create(String name, BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("price must be > 0");
        }
        Product p = new Product(null, name, price, LocalDateTime.now(), null);
        return repository.save(p);
    }

    public Product get(Long id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found."));
    }

    public Product update(Long id, String name, BigDecimal price) {
        Product current = get(id);
        if (name != null && !name.isBlank()) current.setName(name);
        if (price != null) {
            if (price.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Price must be > 0");
            current.setPrice(price);
        }
        current.setUpdatedAt(LocalDateTime.now());
        return repository.save(current);
    }

    public void delete(Long id) { repository.deleteById(id); }

    public List<Product> list() {
        return repository.findAll();
    }

    public Page<Product> paginatedList(int pageNumber, int pageSize) {
        int pn = Math.max(pageNumber, 1) - 1;
        int ps = Math.min(Math.max(pageSize, 1), 100);
        Pageable pageable = PageRequest.of(pn, ps);
        return repository.findAllPaginatedList(pageable);
    }

}
