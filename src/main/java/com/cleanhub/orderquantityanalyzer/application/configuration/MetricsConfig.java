package com.cleanhub.orderquantityanalyzer.application.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public Counter customerRetrievalSuccessCounter(MeterRegistry meterRegistry) {
        return Counter.builder("customer.retrieval.success")
                .description("Counts successful customer retrievals")
                .register(meterRegistry);
    }

    @Bean
    public Counter customerRetrievalFailureCounter(MeterRegistry meterRegistry) {
        return Counter.builder("customer.retrieval.failure")
                .description("Counts failed customer retrievals")
                .register(meterRegistry);
    }

    @Bean
    public Counter orderRetrievalSuccessCounter(MeterRegistry meterRegistry) {
        return Counter.builder("order.retrieval.success")
                .description("Counts successful order retrievals")
                .register(meterRegistry);
    }

    @Bean
    public Counter orderRetrievalFailureCounter(MeterRegistry meterRegistry) {
        return Counter.builder("order.retrieval.failure")
                .description("Counts failed order retrievals")
                .register(meterRegistry);
    }
}
