package org.acme.adapters.in.rest;

/**
 * DTO for Product REST responses.
 */
public class ProductResponse {
    public Long id;
    public String name;
    public String description;

    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}

