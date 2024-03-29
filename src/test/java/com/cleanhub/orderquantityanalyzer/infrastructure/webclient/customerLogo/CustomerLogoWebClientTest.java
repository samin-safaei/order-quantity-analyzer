package com.cleanhub.orderquantityanalyzer.infrastructure.webclient.customerLogo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomerLogoWebClientTest {

    private CustomerLogosWebClient underTest;
    private MockWebServer mockWebServer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${cleanhub.logos.uri}")
    private String customerLogoUri;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        String baseUrl = mockWebServer.url("/").toString();
        underTest = new CustomerLogosWebClient(WebClient.builder(), baseUrl, customerLogoUri);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void fetchCustomers() throws IOException {
        // Load and convert JSON from file
        Path jsonPath = Paths.get(new ClassPathResource("test-customers.json").getFile().getAbsolutePath());
        List<CustomerLogo> expectedCustomers = objectMapper.readValue(
                Files.newInputStream(jsonPath),
                new TypeReference<>() {
                });

        // Prepare mock server response
        mockWebServer.enqueue(new MockResponse()
                .setBody(Files.readString(jsonPath))
                .addHeader("Content-Type", "application/json"));

        // Fetch and block for result for testing
        List<CustomerLogo> result = underTest.fetchCustomers().collectList().block();

        // Assertions
        assertNotNull(result);
        assertEquals(expectedCustomers.size(), result.size());
        for (CustomerLogo expectedCustomer : expectedCustomers) {
            CustomerLogo actualCustomerLogo = result.stream()
                    .filter(customer -> customer.companyName().equals(expectedCustomer.companyName()))
                    .findFirst().orElse(null);
            assertNotNull(actualCustomerLogo);
            assertEquals(expectedCustomer, actualCustomerLogo);
        }
    }
}