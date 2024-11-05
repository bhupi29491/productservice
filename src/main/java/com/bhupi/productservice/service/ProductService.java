package com.bhupi.productservice.service;

import com.bhupi.productservice.dto.Product;

import java.util.List;

public interface ProductService {

    Product addProduct(Product product);

    Product updateProduct(Long id, Product updateProduct);

    void deleteProduct(Long id);

    Product getProductById(Long id);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByPriceRange(double minPrice, double maxPrice);

}
