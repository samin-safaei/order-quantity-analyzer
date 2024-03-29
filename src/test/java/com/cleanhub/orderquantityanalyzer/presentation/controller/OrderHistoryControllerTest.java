package com.cleanhub.orderquantityanalyzer.presentation.controller;

import com.cleanhub.orderquantityanalyzer.application.orderHistory.OrderHistoryApplicationService;
import com.cleanhub.orderquantityanalyzer.application.orderHistory.OrderHistoryDetailParams;
import com.cleanhub.orderquantityanalyzer.application.orderHistory.OrderHistoryDetailResponse;
import com.cleanhub.orderquantityanalyzer.testutil.DateUtil;
import com.cleanhub.orderquantityanalyzer.testutil.TestOrderHistoryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderHistoryController.class)
public class OrderHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderHistoryApplicationService orderHistoryApplicationService;

    @Test
    public void testGetTopCustomersByQuantityChange() throws Exception {
        // WHEN
        List<OrderHistoryDetailResponse> expectedResponses = TestOrderHistoryFactory.getOrderHistoryDetailResponses();
        when(orderHistoryApplicationService.getTopCustomersByQuantityChange(any(OrderHistoryDetailParams.class)))
                .thenReturn(expectedResponses);
        OrderHistoryDetailParams expectedOrderHistoryDetailParams =
                new OrderHistoryDetailParams(0, 10,
                        DateUtil.fromString("2024-03-20 01:01:01"),
                        DateUtil.fromString("2024-03-30 01:01:01"));

        // GIVEN
        mockMvc.perform(post("/orderHistory/topCustomersByQuantityChange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "pageNumber": 0,
                                    "numberOfCompanies" : 10,
                                    "fromDateTime": "2024-03-20 01:01:01",
                                    "toDateTime": "2024-03-30 01:01:01"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(expectedResponses.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerId").value(expectedResponses.get(0).customerId().toString()));

        // THEN
        verify(orderHistoryApplicationService, times(1))
                .getTopCustomersByQuantityChange(expectedOrderHistoryDetailParams);
    }
}
