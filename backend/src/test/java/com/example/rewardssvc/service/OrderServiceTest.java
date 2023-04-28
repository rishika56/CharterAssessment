package com.example.rewardssvc.service;

import com.example.rewardssvc.model.Customer;
import com.example.rewardssvc.model.Order;
import com.example.rewardssvc.repository.CustomerRepository;
import com.example.rewardssvc.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private CustomerRepository customerRepository;
    @MockBean
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        Customer customer1 = Customer.builder().customerId(1L).build();
        Customer customer2 = Customer.builder().customerId(2L).build();
        Order order1 = Order.builder().orderId(1L).customer(customer1).build();
        Order order2 = Order.builder().orderId(2L).customer(customer2).build();

        Mockito.when(customerRepository.findById(customer1.getCustomerId()))
                .thenReturn(Optional.of(customer1));
        Mockito.when(customerRepository.findById(customer2.getCustomerId()))
                .thenReturn(Optional.of(customer2));

        Mockito.when(orderRepository.findByCustomer(customer1))
                .thenReturn(List.of(order1));

        Mockito.when(orderRepository.findByCustomer(customer2))
                .thenReturn(emptyList());

        Mockito.when(orderRepository.findById(order1.getOrderId()))
                .thenReturn(Optional.of(order1));
        doThrow(NoSuchElementException.class)
                .when(orderRepository).findById(61L);

        Mockito.when(orderRepository.findAll())
                .thenReturn(List.of(order1, order2));

    }

    @Test
    public void getAllOrders_test() {
        Long customerId = 1L;
        List<Order> allOrders = orderService.getAllOrders();

        assertNotNull(allOrders);
        assertFalse(allOrders.isEmpty());
        assertEquals(2, allOrders.size());
        assertNotNull(allOrders.get(0).getCustomer());
        assertEquals(customerId, allOrders.get(0).getCustomer().getCustomerId());
        verify(orderRepository).findAll();
    }

    @Test
    public void getOrderForOrderId_test() {
        Long orderId = 1L;
        Long customerId = 1L;
        Order order = orderService.getOrder(orderId);

        assertNotNull(order);
        assertEquals(orderId, order.getOrderId());
        assertNotNull(order.getCustomer());
        assertEquals(customerId, order.getCustomer().getCustomerId());
        verify(orderRepository).findById(orderId);
    }

    @Test
    public void getOrdersForOrderIdNotExist_test() {
        Exception exception = assertThrows(NoSuchElementException.class, () -> orderService.getOrder(61L));

        assertNotNull(exception);
        assertNull(exception.getLocalizedMessage());
        verify(orderRepository).findById(any());
    }

    @Test
    public void searchOrdersForOrderName_test() {
        Long customerId = 1L;
        List<Order> allOrders = orderService.searchOrders(customerId);

        assertNotNull(allOrders);
        assertFalse(allOrders.isEmpty());
        assertEquals(1, allOrders.size());
        assertNotNull(allOrders.get(0).getCustomer());
        assertEquals(customerId, allOrders.get(0).getCustomer().getCustomerId());
        verify(orderRepository).findByCustomer(any());
    }

    @Test
    public void searchOrdersForOrderIdNotExists_test() {
        Exception exception = assertThrows(NoSuchElementException.class, () -> orderService.searchOrders(2L));

        assertNotNull(exception);
        assertEquals("No orders found", exception.getLocalizedMessage());
        verify(orderRepository).findByCustomer(any());
    }

    @Test
    public void searchOrdersForOrderNameAsNull_test() {
        Long customerId = 1L;
        List<Order> allOrders = orderService.searchOrders(null);

        assertNotNull(allOrders);
        assertFalse(allOrders.isEmpty());
        assertEquals(2, allOrders.size());
        assertNotNull(allOrders.get(0).getCustomer());
        assertEquals(customerId, allOrders.get(0).getCustomer().getCustomerId());
        verify(orderRepository).findAll();
    }

    @Test
    public void saveOrder_test() {

        Customer customer1 = Customer.builder().customerId(1L).build();
        Order order1 = Order.builder().orderId(1L).customer(customer1).build();
        Mockito.when(orderRepository.saveAndFlush(any())).thenReturn(order1);

        Order savedOrder = orderService.save(Order.builder().customer(customer1).build());

        assertNotNull(savedOrder);
        assertNotNull(savedOrder.getOrderId());
        assertEquals(1L, savedOrder.getOrderId());
        verify(orderRepository).saveAndFlush(any());
    }
}
