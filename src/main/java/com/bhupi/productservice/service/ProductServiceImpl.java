package com.bhupi.productservice.service;

import com.bhupi.productservice.dto.Product;
import com.bhupi.productservice.exception.ProductNotFoundException;
import com.bhupi.productservice.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product addProduct(Product product) {
        // Perform validation checks for the product entity
        // For example, you can use javax.validation annotations on the Product class

        // Set created_date and updated_date
        product.setCreatedDate(LocalDateTime.now());
        product.setUpdatedDate(LocalDateTime.now());

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = getProductById(id);
        if (existingProduct == null) {
            throw new ProductNotFoundException("Product with ID " + id + " not found");
        }

        // Perform validation checks for the updated product entity
        // For example, you can use javax.validation annotations on the Product class

        // Update the product details
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setImages(updatedProduct.getImages());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setUpdatedDate(LocalDateTime.now());

        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        Product existingProduct = getProductById(id);
        if (existingProduct == null) {
            throw new ProductNotFoundException("Product with ID " + id + " not found");
        }

        productRepository.deleteById(id);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public List<Product> getProductsByPriceRange(double minPrice, double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }
}
