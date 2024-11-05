package com.bhupi.productservice.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import com.bhupi.productservice.dto.Product;
import com.bhupi.productservice.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void testGetAllProducts() throws Exception {
        // Mock the list of products
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Product 1", 10.0));
        products.add(new Product(2L, "Product 2", 20.0));
        when(productService.getAllProducts()).thenReturn(products);

        // Perform the GET request to the controller
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(products.size()))
                .andExpect(jsonPath("$[0].id").value(products.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(products.get(0).getName()))
                .andExpect(jsonPath("$[0].price").value(products.get(0).getPrice()));
    }

    @Test
    public void testGetProductById() throws Exception {
        // Mock a single product
        Product product = new Product(1L, "Product 1", 10.0);
        when(productService.getProductById(1L)).thenReturn(product);

        // Perform the GET request to the controller
        mockMvc.perform(get("/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));
    }

    // Add tests for other controller methods (e.g., create, update, delete) similarly

    @Test
    public void testCreateProduct() throws Exception {
        // Mock a new product
        Product product = new Product(null, "New Product", 15.0);
        Product createdProduct = new Product(1L, "New Product", 15.0);
        when(productService.createProduct(any(Product.class))).thenReturn(createdProduct);

        // Perform the POST request to the controller
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"New Product\", \"price\": 15.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdProduct.getId()))
                .andExpect(jsonPath("$.name").value(createdProduct.getName()))
                .andExpect(jsonPath("$.price").value(createdProduct.getPrice()));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        // Mock an existing product
        Product productToUpdate = new Product(1L, "Product 1", 10.0);
        when(productService.getProductById(1L)).thenReturn(productToUpdate);

        // Mock the updated product
        Product updatedProduct = new Product(1L, "Updated Product", 25.0);
        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(updatedProduct);

        // Perform the PUT request to the controller
        mockMvc.perform(put("/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Product\", \"price\": 25.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedProduct.getId()))
                .andExpect(jsonPath("$.name").value(updatedProduct.getName()))
                .andExpect(jsonPath("$.price").value(updatedProduct.getPrice()));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        // Mock an existing product
        Product productToDelete = new Product(1L, "Product 1", 10.0);
        when(productService.getProductById(1L)).thenReturn(productToDelete);

        // Perform the DELETE request to the controller
        mockMvc.perform(delete("/products/{id}", 1L))
                .andExpect(status().isOk());

        // Verify that the delete method was called with the correct product ID
        verify(productService, times(1)).deleteProduct(1L);
    }

}
