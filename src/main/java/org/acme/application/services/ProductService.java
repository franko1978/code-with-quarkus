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
 * Validates business logic and handles service-level errors.
 */
@ApplicationScoped
@Transactional
public class ProductService {

    @Inject
    ProductRepository productRepository;

    /**
     * Creates a new product with validation.
     *
     * @param name product name (required, 2-255 chars)
     * @param description product description (required, 5-1000 chars)
     * @return created product with ID
     * @throws IllegalArgumentException if validation fails
     */
    public Product createProduct(String name, String description) {
        // Validate inputs at service layer
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Product description cannot be null or empty");
        }

        // Domain object validates business rules via constructor
        Product product = new Product(name, description);
        return productRepository.save(product);
    }

    /**
     * Retrieves a product by ID.
     *
     * @param id product ID
     * @return product if found
     * @throws IllegalArgumentException if ID is invalid
     */
    public Optional<Product> getProductById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Product ID must be a positive number");
        }
        return productRepository.findById(id);
    }

    /**
     * Retrieves all products.
     *
     * @return list of all products (empty list if none exist)
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Deletes a product by ID.
     *
     * @param id product ID to delete
     * @throws IllegalArgumentException if ID is invalid
     */
    public void deleteProduct(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Product ID must be a positive number");
        }
        productRepository.delete(id);
    }
}
