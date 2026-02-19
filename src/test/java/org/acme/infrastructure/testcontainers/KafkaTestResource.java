package org.acme.infrastructure.testcontainers;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;

/**
 * Testcontainers Kafka resource for integration tests.
 */
public class KafkaTestResource implements QuarkusTestResourceLifecycleManager {
    private KafkaContainer kafka;

    @Override
    public Map<String, String> start() {
        kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.1"));
        kafka.start();

        Map<String, String> props = new HashMap<>();
        props.put("kafka.bootstrap.servers", kafka.getBootstrapServers());
        return props;
    }

    @Override
    public void stop() {
        if (kafka != null) {
            kafka.stop();
        }
    }
}
