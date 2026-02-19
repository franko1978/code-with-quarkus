package org.acme.application.ports;

import org.acme.application.events.ProductEventType;

public interface ProductEventPublisher {
    void publish(ProductEventType type, Long productId);
}

