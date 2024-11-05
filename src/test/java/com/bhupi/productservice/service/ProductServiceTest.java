package com.bhupi.productservice.service;

import com.bhupi.productservice.dto.Product;
import com.bhupi.productservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddProduct() {
        // Given
        Product productToAdd = new Product();
        productToAdd.setName("Test Product");
        productToAdd.setCategory("TestCategory");
        productToAdd.setPrice(10.0);

        // When
        when(productRepository.save(any(Product.class))).thenReturn(productToAdd);
        Product addedProduct = productService.addProduct(productToAdd);

        // Then
        assertNotNull(addedProduct.getId());
        assertEquals("Test Product", addedProduct.getName());
        assertEquals("TestCategory", addedProduct.getCategory());
        assertEquals(10.0, addedProduct.getPrice());
        assertNotNull(addedProduct.getCreatedDate());
        assertNotNull(addedProduct.getUpdatedDate());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testUpdateProduct() {
        // Given
        Long productId = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Existing Product");
        existingProduct.setCategory("Existing Category");
        existingProduct.setPrice(50.0);

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setCategory("Updated Category");
        updatedProduct.setPrice(60.0);

        // When
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
        Product result = productService.updateProduct(productId, updatedProduct);

        // Then
        assertEquals("Updated Product", result.getName());
        assertEquals("Updated Category", result.getCategory());
        assertEquals(60.0, result.getPrice());
        assertNotNull(result.getUpdatedDate());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testDeleteProduct() {
        // Given
        Long productId = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(productId);

        // When
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        productService.deleteProduct(productId);

        // Then
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    public void testGetProductById() {
        // Given
        Long productId = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Test Product");
        existingProduct.setCategory("TestCategory");
        existingProduct.setPrice(20.0);

        // When
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        Product retrievedProduct = productService.getProductById(productId);

        // Then
        assertNotNull(retrievedProduct);
        assertEquals(productId, retrievedProduct.getId());
        assertEquals("Test Product", retrievedProduct.getName());
        assertEquals("TestCategory", retrievedProduct.getCategory());
        assertEquals(20.0, retrievedProduct.getPrice());
    }

    @Test
    public void testGetAllProducts() {
        // Given
        List<Product> productList = new ArrayList<>();
        productList.add(new Product(1L, "Product 1", "Category 1","Product 1 description","Product1_Img_Address", 10.0, LocalDateTime.now(), LocalDateTime.now()));
        productList.add(new Product(2L, "Product 2", "Category 2","Product 2 description","Product2_Img_Address", 20.0, LocalDateTime.now(), LocalDateTime.now()));
        productList.add(new Product(3L, "Product 3", "Category 1","Product 3 description","Product3_Img_Address", 15.0, LocalDateTime.now(), LocalDateTime.now()));

        // When
        when(productRepository.findAll()).thenReturn(productList);
        List<Product> result = productService.getAllProducts();

        // Then
        assertEquals(3, result.size());
        assertEquals(productList, result);
    }

    @Test
    public void testGetProductsByCategory() {
        // Given
        List<Product> productList = new ArrayList<>();
        productList.add(new Product(1L, "Product 1", "Category 1","Product 1 description","Product1_Img_Address", 10.0, LocalDateTime.now(), LocalDateTime.now()));
        productList.add(new Product(3L, "Product 3", "Category 1","Product 3 description","Product3_Img_Address", 15.0, LocalDateTime.now(), LocalDateTime.now()));

        // When
        when(productRepository.findByCategory("Category 1")).thenReturn(productList);
        List<Product> result = productService.getProductsByCategory("Category 1");

        // Then
        assertEquals(2, result.size());
        assertEquals(productList, result);
    }

    @Test
    public void testGetProductsByPriceRange() {
        // Given
        List<Product> productList = new ArrayList<>();
        productList.add(new Product(1L, "Product 1", "Category 1","Product 1 description","Product1_Img_Address", 10.0, LocalDateTime.now(), LocalDateTime.now()));
        productList.add(new Product(3L, "Product 3", "Category 1", "Product 2 description","Product2_Img_Address",15.0, LocalDateTime.now(), LocalDateTime.now()));

        // When
        when(productRepository.findByPriceBetween(10.0, 15.0)).thenReturn(productList);
        List<Product> result = productService.getProductsByPriceRange(10.0, 15.0);

        // Then
        assertEquals(2, result.size());
        assertEquals(productList, result);
    }
}

