package com.stockify.client.service;

import com.stockify.client.dto.ProductDTO;
import com.stockify.client.model.Product;
import com.stockify.client.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product productEntity = productDTO.toEntity();
        productEntity = productRepository.save(productEntity);
        return ProductDTO.fromEntity(productEntity);
    }
}
