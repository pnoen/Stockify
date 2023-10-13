package com.stockify.productservice.controller;

import com.stockify.productservice.dto.*;
import com.stockify.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddRequest addRequest) {
        return productService.addProduct(addRequest);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@RequestBody DeleteRequest deleteRequest) {
        return productService.deleteProduct(deleteRequest);
    }

    @GetMapping("/getAll")
    public ResponseEntity<ProductListResponse> getProducts() {
        return productService.getProductList();
    }

    @GetMapping("/get")
    public ResponseEntity<GetProductResponse> getProduct(@RequestParam int id) {
        return productService.getProduct(id);
    }

    @GetMapping("/getInventory")
    public ResponseEntity<InventoryResponse> getInventory(@RequestParam int businessCode) {
        return productService.getInventory(businessCode);
    }

    @PostMapping("/edit")
    public ResponseEntity<ApiResponse> editProduct(@RequestBody EditProductRequest editProductRequest) {
        return productService.editProduct(editProductRequest);
    }

    @GetMapping("/getProducts")
    public ResponseEntity<ProductListSpecificResponse> getProductsList(@RequestParam List<Integer> ids) {
        return productService.getProductListSpecific(ids);
    }

    @GetMapping("/getProductsBusinessList")
    public ResponseEntity<ProductListSpecificResponse> getProductListBusinessCode(@RequestParam List<Integer> businessCodes) {
        return productService.getProductListBusinessCodes(businessCodes);
    }

    @GetMapping("/getProductsCustomer")
    public ResponseEntity<CustomerProductResponse> getProductsCustomer(@RequestParam String email) {
        return productService.getProductsCustomer(email);
    }
}