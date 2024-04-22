package com.example.auth.services;

import com.example.auth.dtos.ProductRequestDTO;
import com.example.auth.dtos.ProductResponseDTO;
import com.example.auth.entities.Product;
import com.example.auth.repositories.ProductRepository;
import com.example.auth.exceptions.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(ProductRequestDTO productRequestDTO) {
        Product newProduct = new Product(productRequestDTO);
        return productRepository.save(newProduct);
    }

    public List<ProductResponseDTO> getAllProducts(){
        List<Product> productList = productRepository.findAll();
        return productList.stream()
                .map(ProductResponseDTO::new)
                .collect(Collectors.toList());
    }
}
