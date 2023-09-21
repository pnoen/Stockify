package com.stockify.productservice;

import com.stockify.productservice.controller.ProductController;
import com.stockify.productservice.dto.*;
import com.stockify.productservice.model.Product;
import com.stockify.productservice.repository.ProductRepository;
import com.stockify.productservice.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(productRepository.save(any(Product.class))).thenReturn(new Product());
    }

    @Test
    public void testValidAddProduct(){
        AddRequest addRequest = new AddRequest();
        addRequest.setName("ProductName");
        addRequest.setDescription("Description");
        addRequest.setPrice((float)2.5);
        addRequest.setQuantity(10);
        addRequest.setCompanyID(1);

        ResponseEntity<ApiResponse> response = productService.addProduct(addRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product Added successfully.", response.getBody().getMessage());
    }

    @Test
    public void testInvalidAddProductPrice(){
        AddRequest addRequest = new AddRequest();
        addRequest.setName("ProductName");
        addRequest.setDescription("Description");
        addRequest.setPrice((float)0);
        addRequest.setQuantity(10);
        addRequest.setCompanyID(1);

        ResponseEntity<ApiResponse> response = productService.addProduct(addRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Quantity or Price cannot be 0", response.getBody().getMessage());
    }

    @Test
    public void testInvalidAddProductQuantity(){
        AddRequest addRequest = new AddRequest();
        addRequest.setName("ProductName");
        addRequest.setDescription("Description");
        addRequest.setPrice((float)2);
        addRequest.setQuantity(0);
        addRequest.setCompanyID(1);

        ResponseEntity<ApiResponse> response = productService.addProduct(addRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Quantity or Price cannot be 0", response.getBody().getMessage());
    }

    @Test
    public void testInvalidAddProductName(){
        AddRequest addRequest = new AddRequest();
        addRequest.setName("");
        addRequest.setDescription("Description");
        addRequest.setPrice((float)2);
        addRequest.setQuantity(1);
        addRequest.setCompanyID(1);

        ResponseEntity<ApiResponse> response = productService.addProduct(addRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: All fields are required.", response.getBody().getMessage());
    }

    @Test
    public void testInvalidAddProductDescription(){
        AddRequest addRequest = new AddRequest();
        addRequest.setName("ProductName");
        addRequest.setDescription("");
        addRequest.setPrice((float)2);
        addRequest.setQuantity(2);
        addRequest.setCompanyID(1);

        ResponseEntity<ApiResponse> response = productService.addProduct(addRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: All fields are required.", response.getBody().getMessage());
    }

    @Test
    public void testValidDeleteProduct() {
        Product product = new Product();
        product.setId(2);
        product.setQuantity(20);
        product.setName("Product");
        product.setDescription("Description");
        product.setCompanyID(2);
        product.setPrice(20);

        Optional<Product> optionalProduct = Optional.ofNullable(product);

        when(productRepository.findById(2)).thenReturn(optionalProduct);

        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.setId(2);
        ResponseEntity<ApiResponse> response = productService.deleteProduct(deleteRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product Deleted successfully.", response.getBody().getMessage());
    }

    @Test
    public void testInvalidDeleteProduct() {
        when(productRepository.findById(2)).thenReturn(Optional.empty());
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.setId(2);
        ResponseEntity<ApiResponse> response = productService.deleteProduct(deleteRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Product does not exist", response.getBody().getMessage());
    }

    @Test
    public void testValidGetProduct() {
        Product product = new Product();
        product.setId(2);
        product.setQuantity(20);
        product.setName("Product");
        product.setDescription("Description");
        product.setCompanyID(2);
        product.setPrice(20);

        Optional<Product> optionalProduct = Optional.ofNullable(product);

        when(productRepository.findById(2)).thenReturn(optionalProduct);

        GetProductResponse productResponse = new GetProductResponse(
                200,
                "Product",
                "Description",
                20,
                20,
                "TEMP COMPANY"
        );

        ResponseEntity<GetProductResponse> response = productService.getProduct(2);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product", response.getBody().getName());
        assertEquals("Description", response.getBody().getDescription());
        assertEquals(20, response.getBody().getQuantity());
        assertEquals(20, response.getBody().getPrice());
        assertEquals("TEMP COMPANY", response.getBody().getCompany());

    }

    @Test
    public void testInvalidGetProduct() {
        when(productRepository.findById(2)).thenReturn(Optional.empty());

        ResponseEntity<GetProductResponse> response = productService.getProduct(2);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Product does not exist", response.getBody().getMessage());

    }

    @Test
    public void testInvalidGetProductList() {
        List<Product> productList = new ArrayList<>();

        when(productRepository.findAll()).thenReturn(productList);

        ResponseEntity<ProductListResponse> response = productService.getProductList();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(new ArrayList<>(), response.getBody().getProducts());

    }

    @Test
    public void testValidGetProductList() {
        Product product = new Product();
        product.setName("ProductName");
        product.setDescription("Description");
        product.setPrice((float)2.5);
        product.setQuantity(10);
        product.setCompanyID(1);
        List<Product> productList = new ArrayList<>();
        productList.add(product);

        when(productRepository.findAll()).thenReturn(productList);

        ResponseEntity<ProductListResponse> response = productService.getProductList();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getProducts().size());
        assertEquals(2.5, response.getBody().getProducts().get(0).getPrice());
        assertEquals("ProductName", response.getBody().getProducts().get(0).getName());
        assertEquals("TEMP COMPANY", response.getBody().getProducts().get(0).getCompanyName());

    }

    @Test
    public void testValidGetInventoryList() {
        Product product = new Product();
        product.setName("ProductName");
        product.setDescription("Description");
        product.setPrice((float)2.5);
        product.setQuantity(10);
        product.setCompanyID(1);
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        when(productRepository.findByCompanyID(2)).thenReturn(productList);
        ResponseEntity<InventoryResponse> response = productService.getInventory(2);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getInventory().size());
        assertEquals(2.5, response.getBody().getInventory().get(0).getPrice());
        assertEquals(10, response.getBody().getInventory().get(0).getQuantity());
        assertEquals("ProductName", response.getBody().getInventory().get(0).getName());
    }

    @Test
    public void testInvalidGetInventoryList() {
        List<Product> productList = new ArrayList<>();
        when(productRepository.findByCompanyID(2)).thenReturn(productList);
        ResponseEntity<InventoryResponse> response = productService.getInventory(2);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(new ArrayList<>(), response.getBody().getInventory());
    }

    @Test
    public void testInvalidEditProduct() {

        EditProductRequest request = new EditProductRequest(
                2,
                "",
                "",
                0,
                0
        );
        ResponseEntity<ApiResponse> response = productService.editProduct(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: At least one field is required", response.getBody().getMessage());
    }

    @Test
    public void testInvalidEditProductException() {

        EditProductRequest request = new EditProductRequest(
                2,
                "New Name",
                "New Description",
                10,
                10
        );
        ResponseEntity<ApiResponse> response = productService.editProduct(request);

        Product product = new Product();
        product.setId(2);
        product.setQuantity(20);
        product.setName("Product");
        product.setDescription("Description");
        product.setCompanyID(2);
        product.setPrice(20);

        Optional<Product> optionalProduct = Optional.ofNullable(product);
        when(productRepository.findById(2)).thenReturn(Optional.empty());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error occurred when attempted to update fields in database", response.getBody().getMessage());

    }

    @Test
    public void testValidEditProduct() {

        EditProductRequest request = new EditProductRequest(
                2,
                "New Name",
                "New Description",
                10,
                10
        );
        Product product = new Product();
        product.setId(2);
        product.setQuantity(20);
        product.setName("Product");
        product.setDescription("Description");
        product.setCompanyID(2);
        product.setPrice(20);

        Optional<Product> optionalProduct = Optional.ofNullable(product);

        when(productRepository.findById(2)).thenReturn(optionalProduct);
        when(productRepository.getReferenceById(2)).thenReturn(product);

        ResponseEntity<ApiResponse> response = productService.editProduct(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product Edited successfully.", response.getBody().getMessage());

    }
    @Test
    public void testValidEditProductEmptyName() {

        EditProductRequest request = new EditProductRequest(
                2,
                "",
                "New Description",
                10,
                10
        );
        Product product = new Product();
        product.setId(2);
        product.setQuantity(20);
        product.setName("Product");
        product.setDescription("Description");
        product.setCompanyID(2);
        product.setPrice(20);

        Optional<Product> optionalProduct = Optional.ofNullable(product);

        when(productRepository.findById(2)).thenReturn(optionalProduct);
        when(productRepository.getReferenceById(2)).thenReturn(product);

        ResponseEntity<ApiResponse> response = productService.editProduct(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product Edited successfully.", response.getBody().getMessage());

    }

    @Test
    public void testValidEditProductEmptyDescription() {

        EditProductRequest request = new EditProductRequest(
                2,
                "New Name",
                "",
                10,
                10
        );
        Product product = new Product();
        product.setId(2);
        product.setQuantity(20);
        product.setName("Product");
        product.setDescription("Description");
        product.setCompanyID(2);
        product.setPrice(20);

        Optional<Product> optionalProduct = Optional.ofNullable(product);

        when(productRepository.findById(2)).thenReturn(optionalProduct);
        when(productRepository.getReferenceById(2)).thenReturn(product);

        ResponseEntity<ApiResponse> response = productService.editProduct(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product Edited successfully.", response.getBody().getMessage());

    }

    @Test
    public void testValidEditProductEmptyQuantity() {

        EditProductRequest request = new EditProductRequest(
                2,
                "New Name",
                "New Description",
                0,
                10
        );
        Product product = new Product();
        product.setId(2);
        product.setQuantity(20);
        product.setName("Product");
        product.setDescription("Description");
        product.setCompanyID(2);
        product.setPrice(20);

        Optional<Product> optionalProduct = Optional.ofNullable(product);

        when(productRepository.findById(2)).thenReturn(optionalProduct);
        when(productRepository.getReferenceById(2)).thenReturn(product);

        ResponseEntity<ApiResponse> response = productService.editProduct(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product Edited successfully.", response.getBody().getMessage());

    }

    @Test
    public void testValidEditProductEmptyPrice() {

        EditProductRequest request = new EditProductRequest(
                2,
                "New Name",
                "New Description",
                10,
                0
        );
        Product product = new Product();
        product.setId(2);
        product.setQuantity(20);
        product.setName("Product");
        product.setDescription("Description");
        product.setCompanyID(2);
        product.setPrice(20);

        Optional<Product> optionalProduct = Optional.ofNullable(product);

        when(productRepository.findById(2)).thenReturn(optionalProduct);
        when(productRepository.getReferenceById(2)).thenReturn(product);

        ResponseEntity<ApiResponse> response = productService.editProduct(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product Edited successfully.", response.getBody().getMessage());

    }

    @Test
    public void testValidEditProduct2Empty1() {

        EditProductRequest request = new EditProductRequest(
                2,
                "",
                "",
                1,
                10
        );
        Product product = new Product();
        product.setId(2);
        product.setQuantity(20);
        product.setName("Product");
        product.setDescription("Description");
        product.setCompanyID(2);
        product.setPrice(20);

        Optional<Product> optionalProduct = Optional.ofNullable(product);

        when(productRepository.findById(2)).thenReturn(optionalProduct);
        when(productRepository.getReferenceById(2)).thenReturn(product);

        ResponseEntity<ApiResponse> response = productService.editProduct(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product Edited successfully.", response.getBody().getMessage());

    }

    @Test
    public void testValidEditProduct2Empty2() {

        EditProductRequest request = new EditProductRequest(
                2,
                "",
                "description",
                0,
                10
        );
        Product product = new Product();
        product.setId(2);
        product.setQuantity(20);
        product.setName("Product");
        product.setDescription("Description");
        product.setCompanyID(2);
        product.setPrice(20);

        Optional<Product> optionalProduct = Optional.ofNullable(product);

        when(productRepository.findById(2)).thenReturn(optionalProduct);
        when(productRepository.getReferenceById(2)).thenReturn(product);

        ResponseEntity<ApiResponse> response = productService.editProduct(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product Edited successfully.", response.getBody().getMessage());

    }

    @Test
    public void testValidEditProduct2Empty3() {

        EditProductRequest request = new EditProductRequest(
                2,
                "",
                "description",
                10,
                0
        );
        Product product = new Product();
        product.setId(2);
        product.setQuantity(20);
        product.setName("Product");
        product.setDescription("Description");
        product.setCompanyID(2);
        product.setPrice(20);

        Optional<Product> optionalProduct = Optional.ofNullable(product);

        when(productRepository.findById(2)).thenReturn(optionalProduct);
        when(productRepository.getReferenceById(2)).thenReturn(product);

        ResponseEntity<ApiResponse> response = productService.editProduct(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product Edited successfully.", response.getBody().getMessage());

    }

    @Test
    public void testValidEditProduct2Empty4() {

        EditProductRequest request = new EditProductRequest(
                2,
                "Name",
                "",
                0,
                10
        );
        Product product = new Product();
        product.setId(2);
        product.setQuantity(20);
        product.setName("Product");
        product.setDescription("Description");
        product.setCompanyID(2);
        product.setPrice(20);

        Optional<Product> optionalProduct = Optional.ofNullable(product);

        when(productRepository.findById(2)).thenReturn(optionalProduct);
        when(productRepository.getReferenceById(2)).thenReturn(product);

        ResponseEntity<ApiResponse> response = productService.editProduct(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product Edited successfully.", response.getBody().getMessage());

    }

    @Test
    public void testValidEditProduct2Empty5() {

        EditProductRequest request = new EditProductRequest(
                2,
                "Name",
                "",
                10,
                0
        );
        Product product = new Product();
        product.setId(2);
        product.setQuantity(20);
        product.setName("Product");
        product.setDescription("Description");
        product.setCompanyID(2);
        product.setPrice(20);

        Optional<Product> optionalProduct = Optional.ofNullable(product);

        when(productRepository.findById(2)).thenReturn(optionalProduct);
        when(productRepository.getReferenceById(2)).thenReturn(product);

        ResponseEntity<ApiResponse> response = productService.editProduct(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product Edited successfully.", response.getBody().getMessage());

    }

    @Test
    public void testValidEditProduct2Empty6() {

        EditProductRequest request = new EditProductRequest(
                2,
                "Name",
                "Description",
                0,
                0
        );
        Product product = new Product();
        product.setId(2);
        product.setQuantity(20);
        product.setName("Product");
        product.setDescription("Description");
        product.setCompanyID(2);
        product.setPrice(20);

        Optional<Product> optionalProduct = Optional.ofNullable(product);

        when(productRepository.findById(2)).thenReturn(optionalProduct);
        when(productRepository.getReferenceById(2)).thenReturn(product);

        ResponseEntity<ApiResponse> response = productService.editProduct(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product Edited successfully.", response.getBody().getMessage());

    }

    @Test
    public void testValidEditProduct3Empty1() {

        EditProductRequest request = new EditProductRequest(
                2,
                "",
                "",
                0,
                10
        );
        Product product = new Product();
        product.setId(2);
        product.setQuantity(20);
        product.setName("Product");
        product.setDescription("Description");
        product.setCompanyID(2);
        product.setPrice(20);

        Optional<Product> optionalProduct = Optional.ofNullable(product);

        when(productRepository.findById(2)).thenReturn(optionalProduct);
        when(productRepository.getReferenceById(2)).thenReturn(product);

        ResponseEntity<ApiResponse> response = productService.editProduct(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product Edited successfully.", response.getBody().getMessage());

    }

    @Test
    public void testValidEditProduct3Empty2() {

        EditProductRequest request = new EditProductRequest(
                2,
                "",
                "",
                10,
                0
        );
        Product product = new Product();
        product.setId(2);
        product.setQuantity(20);
        product.setName("Product");
        product.setDescription("Description");
        product.setCompanyID(2);
        product.setPrice(20);

        Optional<Product> optionalProduct = Optional.ofNullable(product);

        when(productRepository.findById(2)).thenReturn(optionalProduct);
        when(productRepository.getReferenceById(2)).thenReturn(product);

        ResponseEntity<ApiResponse> response = productService.editProduct(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product Edited successfully.", response.getBody().getMessage());

    }

    @Test
    public void testValidEditProduct3Empty3() {

        EditProductRequest request = new EditProductRequest(
                2,
                "",
                "Description",
                0,
                0
        );
        Product product = new Product();
        product.setId(2);
        product.setQuantity(20);
        product.setName("Product");
        product.setDescription("Description");
        product.setCompanyID(2);
        product.setPrice(20);

        Optional<Product> optionalProduct = Optional.ofNullable(product);

        when(productRepository.findById(2)).thenReturn(optionalProduct);
        when(productRepository.getReferenceById(2)).thenReturn(product);

        ResponseEntity<ApiResponse> response = productService.editProduct(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product Edited successfully.", response.getBody().getMessage());

    }

    @Test
    public void testValidEditProduct3Empty4() {

        EditProductRequest request = new EditProductRequest(
                2,
                "Name",
                "",
                0,
                0
        );
        Product product = new Product();
        product.setId(2);
        product.setQuantity(20);
        product.setName("Product");
        product.setDescription("Description");
        product.setCompanyID(2);
        product.setPrice(20);

        Optional<Product> optionalProduct = Optional.ofNullable(product);

        when(productRepository.findById(2)).thenReturn(optionalProduct);
        when(productRepository.getReferenceById(2)).thenReturn(product);

        ResponseEntity<ApiResponse> response = productService.editProduct(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product Edited successfully.", response.getBody().getMessage());

    }





}
