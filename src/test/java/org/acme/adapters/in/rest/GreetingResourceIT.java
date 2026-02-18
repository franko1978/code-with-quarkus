package org.acme.adapters.in.rest;

import io.quarkus.test.junit.QuarkusIntegrationTest;

/**
 * Integration tests for GreetingResource in packaged mode.
 * Extends GreetingResourceTest to run the same tests against the packaged application.
 */
@QuarkusIntegrationTest
class GreetingResourceIT extends GreetingResourceTest {
    // Execute the same tests but in packaged mode.
}

