package org.acme.adapters.out.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

/**
 * JPA entity for Product persistence.
 * This adapter-layer entity is only used by Panache; domain Product is framework-agnostic.
 */
@Entity(name = "products")
public class ProductEntity extends PanacheEntity {
    public String name;
    public String description;
}

