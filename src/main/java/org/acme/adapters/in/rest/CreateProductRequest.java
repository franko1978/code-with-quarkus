package org.acme.adapters.in.rest;

/**
 * DTO for Product REST requests.
 */
public class CreateProductRequest {
    public String name;
    public String description;

    public CreateProductRequest() {
    }

    public CreateProductRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

