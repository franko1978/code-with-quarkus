package org.acme.adapters.in.rest;

import org.acme.application.services.ProductService;
import org.acme.domain.Product;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * REST adapter (inbound) for Product management.
 * Calls the ProductService (application layer).
 */
@Path("/api/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductService productService;

    @POST
    public Response createProduct(CreateProductRequest request) {
        Product product = productService.createProduct(request.name, request.description);
        return Response.ok(toResponse(product)).status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/{id}")
    public Response getProduct(@PathParam("id") Long id) {
        return productService.getProductById(id)
                .map(p -> Response.ok(toResponse(p)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id) {
        productService.deleteProduct(id);
        return Response.noContent().build();
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getDescription());
    }
}

