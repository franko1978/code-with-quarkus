package org.acme.adapters.out.persistence;

import org.acme.application.ports.ProductRepository;
import org.acme.domain.Product;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

/**
 * Panache repository adapter (outbound) implementing the ProductRepository port.
 * This adapter couples the Product JPA entity to the domain Product class.
 */
@ApplicationScoped
public class ProductPanacheRepository implements ProductRepository {

    @Override
    public Product save(Product product) {
        ProductEntity entity = new ProductEntity();
        entity.name = product.getName();
        entity.description = product.getDescription();
        entity.persist();
        product.setId(entity.id);
        return product;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return ProductEntity.<ProductEntity>findByIdOptional(id)
                .map(this::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return ProductEntity.<ProductEntity>listAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void delete(Long id) {
        ProductEntity.deleteById(id);
    }

    private Product toDomain(ProductEntity entity) {
        return new Product(entity.id, entity.name, entity.description);
    }
}

