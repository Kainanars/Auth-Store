package com.example.auth.dtos;

import com.example.auth.entities.Product;

public record ProductResponseDTO(String id, String name, Float price) {
    public ProductResponseDTO(Product product){
        this(product.getId(), product.getName(), product.getPrice());
    }
}
