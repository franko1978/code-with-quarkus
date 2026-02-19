package org.acme.adapters.out.messaging;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.application.events.ProductEventType;
import org.acme.application.ports.ProductEventPublisher;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Properties;

/**
 * Kafka adapter that publishes product IDs for create/update events.
 */
@ApplicationScoped
public class KafkaProductEventPublisher implements ProductEventPublisher {

    private final KafkaProducer<String, Long> producer;
    private final String createdTopic;
    private final String updatedTopic;

    public KafkaProductEventPublisher(
            @ConfigProperty(name = "kafka.bootstrap.servers", defaultValue = "localhost:9092") String bootstrapServers,
            @ConfigProperty(name = "kafka.product.created.topic") String createdTopic,
            @ConfigProperty(name = "kafka.product.updated.topic") String updatedTopic
    ) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        this.producer = new KafkaProducer<>(props);
        this.createdTopic = createdTopic;
        this.updatedTopic = updatedTopic;
    }

    @Override
    public void publish(ProductEventType type, Long productId) {
        if (productId == null) {
            return;
        }
        String topic = (type == ProductEventType.CREATED) ? createdTopic : updatedTopic;
        producer.send(new ProducerRecord<>(topic, productId));
    }

    @PreDestroy
    void close() {
        producer.close();
    }
}
