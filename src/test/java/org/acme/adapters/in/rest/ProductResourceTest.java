package org.acme.adapters.in.rest;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.acme.infrastructure.testcontainers.PostgresTestResource;
import org.acme.adapters.out.persistence.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.transaction.Transactional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.is;

/**
 * Integration tests for ProductResource REST adapter.
 * Tests CRUD operations on /api/products endpoints.
 */
@QuarkusTest
@QuarkusTestResource(PostgresTestResource.class)
class ProductResourceTest {

    @BeforeEach
    @Transactional
    void setUp() {
        // Clean up and insert test data
        ProductEntity.deleteAll();
        insertTestData();
    }

    private void insertTestData() {
        ProductEntity p1 = new ProductEntity();
        p1.name = "Product 1";
        p1.description = "First test product";
        p1.persist();

        ProductEntity p2 = new ProductEntity();
        p2.name = "Product 2";
        p2.description = "Second test product";
        p2.persist();

        ProductEntity p3 = new ProductEntity();
        p3.name = "Product 3";
        p3.description = "Third test product";
        p3.persist();
    }

    @Test
    void testGetAllProducts() {
        given()
          .when().get("/api/products")
          .then()
             .statusCode(200)
             .body("", hasSize(3));
    }

    @Test
    void testGetProductById() {
        // Get all products and use the first ID
        ProductEntity firstProduct = ProductEntity.<ProductEntity>listAll().stream().findFirst().orElse(null);
        assert firstProduct != null : "No products found";

        given()
          .when().get("/api/products/" + firstProduct.id)
          .then()
             .statusCode(200)
             .body("id", is(firstProduct.id.intValue()))
             .body("name", is("Product 1"));
    }

    @Test
    void testCreateProduct() {
        given()
          .contentType("application/json")
          .body("{\"name\": \"New Product\", \"description\": \"A new product\"}")
          .when().post("/api/products")
          .then()
             .statusCode(201)
             .body("name", is("New Product"));
    }
}

