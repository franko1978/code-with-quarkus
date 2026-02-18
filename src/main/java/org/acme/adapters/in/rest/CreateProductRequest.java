package org.acme.adapters.in.rest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for Product REST requests.
 * Validates input at the REST adapter layer.
 */
public class CreateProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 255, message = "Product name must be between 2 and 255 characters")
    public String name;

    @NotBlank(message = "Product description is required")
    @Size(min = 5, max = 1000, message = "Product description must be between 5 and 1000 characters")
    public String description;

    public CreateProductRequest() {
    }

    public CreateProductRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

