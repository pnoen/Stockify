package com.stockify.productservice.repository;

import com.stockify.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    //Optional<Product> findByCompanyID(int id);
    //List<Product> findByCompanyID(int id);
    List<Product> findByBusinessCode(int id);


}
