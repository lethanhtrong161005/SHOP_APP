package com.example.SHOP_APP.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Long id;
    private String title;
    private BigDecimal price;
    private BigDecimal discountPercentage;
    private Integer stock;
    private String thumbnail;
    private String description;
}
