package com.cleanhub.orderquantityanalyzer;

import com.cleanhub.orderquantityanalyzer.testutil.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class OrderQuantityAnalyzerApplicationTests extends AbstractIntegrationTest {

	@Test
	void contextLoads() {
	}

}
