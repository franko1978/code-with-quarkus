package org.acme.adapters.in.rest;

import org.acme.application.services.ProductService;
import org.acme.domain.Product;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * REST adapter (inbound) for Product management.
 * Calls the ProductService (application layer).
 * Handles request validation and error responses.
 */
@Path("/api/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductService productService;

    /**
     * Creates a new product.
     *
     * @param request validated request with name and description
     * @return 201 Created with product data, or 400 Bad Request if validation fails
     */
    @POST
    public Response createProduct(@Valid CreateProductRequest request) {
        try {
            Product product = productService.createProduct(request.name, request.description);
            return Response.ok(toResponse(product)).status(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("VALIDATION_ERROR", e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred"))
                    .build();
        }
    }

    /**
     * Gets a product by ID.
     *
     * @param id product ID
     * @return 200 OK with product data, or 404 Not Found if product doesn't exist
     */
    @GET
    @Path("/{id}")
    public Response getProduct(@PathParam("id") Long id) {
        try {
            return productService.getProductById(id)
                    .map(p -> Response.ok(toResponse(p)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity(new ErrorResponse("NOT_FOUND", "Product with ID " + id + " not found"))
                            .build());
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("INVALID_ID", e.getMessage()))
                    .build();
        }
    }

    /**
     * Gets all products.
     *
     * @return 200 OK with list of all products (empty if none exist)
     */
    @GET
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Deletes a product by ID.
     *
     * @param id product ID to delete
     * @return 204 No Content on success, or 400 Bad Request if ID is invalid
     */
    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id) {
        try {
            productService.deleteProduct(id);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("INVALID_ID", e.getMessage()))
                    .build();
        }
    }

    /**
     * Updates an existing product by ID.
     *
     * @param id product ID
     * @param request validated request with name and description
     * @return 200 OK with updated product, or 404 if not found
     */
    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") Long id, @Valid CreateProductRequest request) {
        try {
            return productService.updateProduct(id, request.name, request.description)
                    .map(p -> Response.ok(toResponse(p)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity(new ErrorResponse("NOT_FOUND", "Product with ID " + id + " not found"))
                            .build());
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("VALIDATION_ERROR", e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred"))
                    .build();
        }
    }


    private ProductResponse toResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getDescription());
    }
}
