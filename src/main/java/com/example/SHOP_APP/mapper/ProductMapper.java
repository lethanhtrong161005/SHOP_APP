package com.example.SHOP_APP.mapper;

import com.example.SHOP_APP.dto.ProductDTO;
import com.example.SHOP_APP.entities.Products;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProductMapper {
    ProductDTO toProductDTO(Products products);
}
