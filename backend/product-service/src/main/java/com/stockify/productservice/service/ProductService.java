package com.stockify.productservice.service;

import com.stockify.productservice.dto.*;
import com.stockify.productservice.model.*;
import com.stockify.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    public ResponseEntity<ApiResponse> addProduct(AddRequest addRequest) {
        String name = addRequest.getName();
        String description = addRequest.getDescription();
        int quantity = addRequest.getQuantity();
        float price = addRequest.getPrice();
        int businessCode = addRequest.getBusinessCode();

        if(name.isEmpty() || description.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "Error: All fields are required."));
        }
        if(quantity == 0 || price == 0) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "Error: Quantity or Price cannot be 0"));
        }

        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .quantity(quantity)
                .businessCode(businessCode)
                .build();
        productRepository.save(product);
        return ResponseEntity.ok(new ApiResponse(200, "Product Added successfully."));

    }

    public ResponseEntity<ApiResponse> deleteProduct(DeleteRequest deleteRequest) {
        Optional<Product> existingProduct = productRepository.findById(deleteRequest.getId());
        if(!existingProduct.isPresent()) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "Product does not exist"));
        }
        productRepository.deleteById(deleteRequest.getId());
        return ResponseEntity.ok(new ApiResponse(200, "Product Deleted successfully."));
    }

    public ResponseEntity<GetProductResponse> getProduct(int id) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if(!existingProduct.isPresent()) {
            return ResponseEntity.badRequest().body(new GetProductResponse(400, "Product does not exist"));
        }
        Optional<Product> product = productRepository.findById(id);

        //NOTE - INSERT CODE TO GET COMPANY NAME FROM ITS ID
        String companyName = "TEMP COMPANY";

        return ResponseEntity.ok(new GetProductResponse(200, product.get().getName(),
                product.get().getDescription(),
                product.get().getQuantity(),
                product.get().getPrice(),
                companyName));
    }

    public ResponseEntity<ProductListResponse> getProductList() {
        List<ProductsSimple> productList = new ArrayList<>();
        List<Product> products = productRepository.findAll();
        for(Product product: products) {
            //NOTE - INSERT CODE TO GET COMPANY NAME FROM ITS ID
            String companyName = "TEMP COMPANY";

            productList.add(new ProductsSimple(product.getName(), product.getPrice(), companyName));
        }

        if(productList.isEmpty()) {
            return ResponseEntity.badRequest().body(new ProductListResponse(404, new ArrayList<>()));
        }
        return ResponseEntity.ok(new ProductListResponse(200, productList));

    }

    public ResponseEntity<InventoryResponse> getInventory(int businessCode) {

        List<Product> productList = productRepository.findByBusinessCode(businessCode);

        if(productList.isEmpty()) {
            return ResponseEntity.badRequest().body(new InventoryResponse(404, new ArrayList<>()));
        }
        return ResponseEntity.ok(new InventoryResponse(200, productList));
    }

    public ResponseEntity<ApiResponse> editProduct(EditProductRequest editProductRequest) {
        String name = editProductRequest.getName();
        String description = editProductRequest.getDescription();
        float price = editProductRequest.getPrice();
        int quantity = editProductRequest.getQuantity();

        if(name.isEmpty() && description.isEmpty() && price == 0 && quantity == 0) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "Error: At least one field is required"));
        }

        try{
            Optional<Product> productSearch = productRepository.findById(editProductRequest.getId());
            if(!productSearch.isPresent()){
                throw new Exception();
            }
            Product product = productRepository.getReferenceById(editProductRequest.getId());
            if(!name.isEmpty()) {
                product.setName(name);
            }
            if(!description.isEmpty()){
                product.setDescription(description);
            }
            if(price != 0) {
                product.setPrice(price);
            }
            if(quantity!=0) {
                product.setQuantity(quantity);
            }
            productRepository.save(product);
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "Error occurred when attempted to update fields in database"));
        }
        return ResponseEntity.ok(new ApiResponse(200, "Product Edited successfully."));

    }

    public ResponseEntity<ProductListSpecificResponse> getProductListSpecific(List<Integer> ids) {
        List<Product> productList = new ArrayList<>();
        for(int id: ids) {
            Optional<Product> product = productRepository.findById(id);
            if(!product.isPresent()){
                return ResponseEntity.badRequest().body(new ProductListSpecificResponse(400, new ArrayList<>()));
            }productList.add(product.get());

        }
        if(productList.isEmpty()) {
            return ResponseEntity.badRequest().body(new ProductListSpecificResponse(400, new ArrayList<>()));
        }
        return ResponseEntity.ok(new ProductListSpecificResponse(200, productList));

    }

    public ResponseEntity<ProductListSpecificResponse> getProductListBusinessCodes (List<Integer> businessCodes) {
        List<Product> productList = new ArrayList<>();
        for(int businessCode: businessCodes) {
            List<Product> productls = productRepository.findByBusinessCode(businessCode);
            productList.addAll(productls);
        }
        if(productList.isEmpty()) {
            return ResponseEntity.badRequest().body(new ProductListSpecificResponse(400, new ArrayList<>()));
        }
        return ResponseEntity.ok(new ProductListSpecificResponse(200, productList));
    }

}