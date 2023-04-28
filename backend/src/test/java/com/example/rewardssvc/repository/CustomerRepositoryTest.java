package com.example.rewardssvc.repository;

import com.example.rewardssvc.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void givenCustomerRepository_whenSaveAndRetrieveEntity_thenOK() {
        Customer customerEntity = customerRepository.save(Customer.builder().build());
        Optional<Customer> foundEntity = customerRepository.findById(customerEntity.getCustomerId());

        assertTrue(foundEntity.isPresent());
        assertNotNull(foundEntity.get());
        assertEquals(customerEntity.getCustomerId(), foundEntity.get().getCustomerId());
    }

    @Test
    public void givenCustomerRepository_withoutSaveAndRetrieveEntity_thenFail() {
        final Long customerId = 125L;
        Optional<Customer> foundEntity = customerRepository.findById(customerId);

        assertNotNull(foundEntity);
        assertFalse(foundEntity.isPresent());
    }

    @Test
    public void givenCustomerRepository_whenSaveAndRetrieveEntityWithName_thenOK() {
        final String customerName = "John Doe";
        customerRepository.save(Customer.builder().name(customerName).build());
        List<Customer> customers = customerRepository.findByName(customerName);

        assertFalse(customers.isEmpty());
        assertNotNull(customers.get(0));
        assertEquals(customerName, customers.get(0).getName());
    }

    @Test
    public void givenCustomerRepository_whenSaveAndRetrieveEntityWithDifferentName_thenFail() {
        final String customer1Name = "John Doe";
        final String customer2Name = "Joe Drakes";
        customerRepository.save(Customer.builder().name(customer1Name).build());
        List<Customer> customers = customerRepository.findByName(customer2Name);

        assertNotNull(customers);
        assertTrue(customers.isEmpty());
    }
}
