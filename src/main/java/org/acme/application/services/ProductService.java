package org.acme.application.services;

import org.acme.application.ports.ProductRepository;
import org.acme.domain.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Application service (use case) for product management.
 * Depends only on the domain and repository port.
 */
@ApplicationScoped
@Transactional
public class ProductService {

    @Inject
    ProductRepository productRepository;

    public Product createProduct(String name, String description) {
        Product product = new Product(name, description);
        return productRepository.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void deleteProduct(Long id) {
        productRepository.delete(id);
    }
}


