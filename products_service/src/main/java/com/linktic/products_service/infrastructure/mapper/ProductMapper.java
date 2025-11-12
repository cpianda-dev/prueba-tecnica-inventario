package com.linktic.products_service.infrastructure.mapper;

import com.linktic.products_service.domain.model.Product;
import com.linktic.products_service.infrastructure.persistence.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toDomain(ProductEntity entity);

    @Mapping(target = "id", source = "id")
    ProductEntity toEntity(Product domain);
}
