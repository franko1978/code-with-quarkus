package org.acme.application.ports;

import org.acme.domain.Product;

import java.util.List;
import java.util.Optional;

/**
 * Port (inbound interface) for product persistence.
 * Application layer defines the contract; adapters implement it.
 */
public interface ProductRepository {
    Product save(Product product);

    Optional<Product> findById(Long id);

    List<Product> findAll();

    void delete(Long id);

    void deleteAll();
}
