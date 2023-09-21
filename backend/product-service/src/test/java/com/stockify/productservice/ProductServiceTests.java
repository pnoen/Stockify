package com.stockify.productservice;

import com.stockify.productservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


}
