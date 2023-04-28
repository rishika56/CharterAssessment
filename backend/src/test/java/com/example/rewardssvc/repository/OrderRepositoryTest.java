package com.example.rewardssvc.repository;

import com.example.rewardssvc.model.Customer;
import com.example.rewardssvc.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void givenOrderRepository_whenSaveAndRetrieveEntity_thenOK() {
        Customer customer = customerRepository.save(Customer.builder().build());
        Order orderEntity = orderRepository.save(Order.builder().customer(customer).build());
        Optional<Order> foundEntity = orderRepository.findById(orderEntity.getOrderId());

        assertTrue(foundEntity.isPresent());
        assertNotNull(foundEntity.get());
        assertEquals(orderEntity.getOrderId(), foundEntity.get().getOrderId());
    }

    @Test
    public void givenOrderRepository_withoutSaveAndRetrieveEntity_thenFail() {
        final Long customerId = 125L;
        Optional<Order> foundEntity = orderRepository.findById(customerId);

        assertNotNull(foundEntity);
        assertFalse(foundEntity.isPresent());
    }

    @Test
    public void givenOrderRepository_whenSaveAndRetrieveEntityWithCustomerId_thenOK() {
        Customer customer = customerRepository.save(Customer.builder().build());
        orderRepository.save(Order.builder().customer(customer).build());
        List<Order> foundEntity = orderRepository.findByCustomer(customer);

        assertFalse(foundEntity.isEmpty());
        assertNotNull(foundEntity.get(0));
        assertNotNull(foundEntity.get(0).getCustomer());
        assertEquals(customer.getCustomerId(), foundEntity.get(0).getCustomer().getCustomerId());
    }

    @Test
    public void givenOrderRepository_whenSaveAndRetrieveEntityWithDifferentCustomerId_thenFail() {
        Customer customer1 = customerRepository.save(Customer.builder().build());
        Customer customer2 = customerRepository.save(Customer.builder().build());

        orderRepository.save(Order.builder().customer(customer1).build());
        List<Order> foundEntity = orderRepository.findByCustomer(customer2);

        assertNotNull(foundEntity);
        assertTrue(foundEntity.isEmpty());
    }
}
