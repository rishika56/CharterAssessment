package com.example.rewardssvc.controller;

import com.example.rewardssvc.model.Customer;
import com.example.rewardssvc.service.CustomerService;
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

public class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        Long customer1Id = 121L;
        Long customer2Id = 122L;
        String customer1Name = "alex";
        String customer2Name = "bob";
        Customer alex = Customer.builder().customerId(customer1Id).name(customer1Name).build();
        Customer bob = Customer.builder().customerId(customer2Id).name(customer2Name).build();

        Mockito.when(customerService.getCustomer(customer1Id))
                .thenReturn(alex);
        doThrow(NoSuchElementException.class)
                .when(customerService).getCustomer(125L);

        Mockito.when(customerService.searchCustomers(customer1Name))
                .thenReturn(List.of(alex));

        Mockito.when(customerService.searchCustomers(null))
                .thenReturn(List.of(alex, bob));
        Mockito.when(customerService.searchCustomers(""))
                .thenReturn(List.of(alex, bob));

        Mockito.when(customerService.save(any()))
                .thenReturn(alex);
    }

    @Test
    public void givenCustomers_whenGetCustomer_thenStatus200()
            throws Exception {

        mvc.perform(get("/api/v1/customers?customerId=121")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId", is(121)))
                .andExpect(jsonPath("$.name", is("alex")));
    }

    @Test
    public void givenCustomers_whenGetCustomer_thenStatusNotFound()
            throws Exception {

        mvc.perform(get("/api/v1/customers?customerId=125")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("No data found")));
    }

    @Test
    public void givenCustomers_whenSearchCustomersWithParam_thenStatusOk()
            throws Exception {

        mvc.perform(get("/api/v1/customers/search?customerName=alex")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].name", is("alex")));
    }

    @Test
    public void givenCustomers_whenSearchCustomersEmptyParam_thenStatusOk()
            throws Exception {

        mvc.perform(get("/api/v1/customers/search?customerName=")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].name", is("alex")))
                .andExpect(jsonPath("$[1].name", is("bob")));
    }

    @Test
    public void givenCustomers_whenSearchCustomersNoParam_thenStatusOk()
            throws Exception {

        mvc.perform(get("/api/v1/customers/search")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].name", is("alex")))
                .andExpect(jsonPath("$[1].name", is("bob")));
    }

    @Test
    public void givenCustomer_whenSaveCustomer_thenStatusOk()
            throws Exception {

        mvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"fake_data\",\n" +
                                "  \"email\": \"fake_data\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$.customerId", is(121)));
    }
}
