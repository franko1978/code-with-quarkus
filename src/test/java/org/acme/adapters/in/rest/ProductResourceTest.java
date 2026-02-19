package org.acme.adapters.in.rest;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.acme.infrastructure.testcontainers.KafkaTestResource;
import org.acme.infrastructure.testcontainers.PostgresTestResource;
import org.acme.adapters.out.persistence.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.transaction.Transactional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.is;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.eclipse.microprofile.config.ConfigProvider;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * Integration tests for ProductResource REST adapter.
 * Tests CRUD operations on /api/products endpoints.
 */
@QuarkusTest
@QuarkusTestResource(PostgresTestResource.class)
@QuarkusTestResource(KafkaTestResource.class)
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
        Integer id = given()
          .contentType("application/json")
          .body("{\"name\": \"New Product\", \"description\": \"A new product\"}")
          .when().post("/api/products")
          .then()
             .statusCode(201)
             .body("name", is("New Product"))
             .extract().path("id");

        Long eventId = pollForProductId("product-created");
        assert eventId != null && eventId.longValue() == id.longValue() : "Missing product-created event";
    }

    @Test
    void testUpdateProduct() {
        ProductEntity firstProduct = ProductEntity.<ProductEntity>listAll().stream().findFirst().orElse(null);
        assert firstProduct != null : "No products found";

        given()
          .contentType("application/json")
          .body("{\"name\": \"Updated Product\", \"description\": \"Updated description\"}")
          .when().put("/api/products/" + firstProduct.id)
          .then()
             .statusCode(200)
             .body("name", is("Updated Product"));

        Long eventId = pollForProductId("product-updated");
        assert eventId != null && eventId.longValue() == firstProduct.id.longValue() : "Missing product-updated event";
    }

    private Long pollForProductId(String topic) {
        String bootstrapServers = ConfigProvider.getConfig().getValue("kafka.bootstrap.servers", String.class);
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "product-test-" + UUID.randomUUID());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        try (KafkaConsumer<String, Long> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(List.of(topic));
            long deadline = System.currentTimeMillis() + 5000;
            while (System.currentTimeMillis() < deadline) {
                ConsumerRecords<String, Long> records = consumer.poll(Duration.ofMillis(500));
                if (!records.isEmpty()) {
                    return records.iterator().next().value();
                }
            }
        }
        return null;
    }
}
