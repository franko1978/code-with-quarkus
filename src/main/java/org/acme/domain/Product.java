package org.acme.domain;

/**
 * Domain entity - framework agnostic.
 * Pure business model with no persistence annotations.
 * Validates business rules at the domain layer.
 */
public class Product {
    private Long id;
    private String name;
    private String description;

    public Product() {
    }

    public Product(String name, String description) {
        this.setName(name);
        this.setDescription(description);
    }

    public Product(Long id, String name, String description) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        validateDescription(description);
        this.description = description;
    }

    /**
     * Validates product name business rules.
     * Name must be non-null, non-blank, and between 2-255 characters.
     */
    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (name.length() < 2) {
            throw new IllegalArgumentException("Product name must be at least 2 characters");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Product name cannot exceed 255 characters");
        }
    }

    /**
     * Validates product description business rules.
     * Description must be non-null, non-blank, and between 5-1000 characters.
     */
    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Product description cannot be null or empty");
        }
        if (description.length() < 5) {
            throw new IllegalArgumentException("Product description must be at least 5 characters");
        }
        if (description.length() > 1000) {
            throw new IllegalArgumentException("Product description cannot exceed 1000 characters");
        }
    }
}
