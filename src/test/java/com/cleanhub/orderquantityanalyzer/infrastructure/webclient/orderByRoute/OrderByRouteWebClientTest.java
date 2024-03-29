package com.cleanhub.orderquantityanalyzer.infrastructure.webclient.orderByRoute;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
class OrderByRouteWebClientTest {

    private OrderByRouteWebClient underTest;
    private MockWebServer mockWebServer;

    private final String orderByRouteUri = "testUri";

    private final String routeUrlParamName = "testParam";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        String baseUrl = mockWebServer.url("/").toString();
        underTest = new OrderByRouteWebClient(WebClient.builder(), baseUrl, orderByRouteUri, routeUrlParamName);
        logger.info("orderByRouteUri, routeUrlParamName, {}, {}", orderByRouteUri, routeUrlParamName);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void fetchOrdersByRoute_shouldParseResultAsExpected() throws IOException {
        // Load and convert JSON from file
        String expectedRoute = UUID.randomUUID().toString();
        Path jsonPath = Paths.get(new ClassPathResource("test-order.json").getFile().getAbsolutePath());
        OrderByRoute expectedOrder = objectMapper.readValue(
                Files.newInputStream(jsonPath),
                new TypeReference<>() {
                });

        // Prepare mock server response
        // Enqueue the JSON response
        mockWebServer.enqueue(new MockResponse()
                .setBody(Files.readString(jsonPath))
                .addHeader("Content-Type", "application/json"));

        // Fetch and block for result for testing
        OrderByRoute actualOrderByRoute = underTest.fetchOrdersByRoute(expectedRoute).block();

        // Assertions
        assertNotNull(actualOrderByRoute);
        assertEquals(expectedOrder, actualOrderByRoute);
    }
}