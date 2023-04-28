package com.example.rewardssvc.controller;

import com.example.rewardssvc.model.Customer;
import com.example.rewardssvc.model.Order;
import com.example.rewardssvc.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

public class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        Long id1 = 131L;
        Long id2 = 132L;
        Customer customer1 = Customer.builder().customerId(id1).build();
        Customer customer2 = Customer.builder().customerId(id2).build();
        Order order1 = Order.builder().orderId(id1).customer(customer1).build();
        Order order2 = Order.builder().orderId(id2).customer(customer2).build();

        Mockito.when(orderService.getOrder(id1))
                .thenReturn(order1);
        doThrow(NoSuchElementException.class)
                .when(orderService).getOrder(125L);

        Mockito.when(orderService.searchOrders(id1))
                .thenReturn(List.of(order1));

        Mockito.when(orderService.searchOrders(null))
                .thenReturn(List.of(order1, order2));

        Mockito.when(orderService.save(any()))
                .thenReturn(order1);
    }

    @Test
    public void givenOrders_whenGetOrder_thenStatus200()
            throws Exception {

        mvc.perform(get("/api/v1/orders?orderId=131")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderId", is(131)))
                .andExpect(jsonPath("$.customer.customerId", is(131)));
    }

    @Test
    public void givenOrders_whenGetOrder_thenStatusNotFound()
            throws Exception {

        mvc.perform(get("/api/v1/orders?orderId=125")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("No data found")));
    }

    @Test
    public void givenOrders_whenSearchOrdersWithParam_thenStatusOk()
            throws Exception {

        mvc.perform(get("/api/v1/orders/search?customerId=131")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].customer.customerId", is(131)));
    }

    @Test
    public void givenOrders_whenSearchOrdersEmptyParam_thenStatusOk()
            throws Exception {

        mvc.perform(get("/api/v1/orders/search?customerId=")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].customer.customerId", is(131)))
                .andExpect(jsonPath("$[1].customer.customerId", is(132)));
    }

    @Test
    public void givenOrders_whenSearchOrdersNoParam_thenStatusOk()
            throws Exception {

        mvc.perform(get("/api/v1/orders/search")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].customer.customerId", is(131)))
                .andExpect(jsonPath("$[1].customer.customerId", is(132)));
    }

    @Test
    public void givenOrders_whenSaveOrders_thenStatusOk()
            throws Exception {

        mvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"purchaseDate\": \"2015-03-19T04:13:08\",\n" +
                                "  \"purchaseAmount\": 86.89,\n" +
                                "  \"customer\": {\n" +
                                "    \"customerId\": 95,\n" +
                                "    \"name\": \"fake_data\",\n" +
                                "    \"email\": \"fake_data\"\n" +
                                "  }\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(4)))
                .andExpect(jsonPath("$.customer.customerId", is(131)));
    }
}
