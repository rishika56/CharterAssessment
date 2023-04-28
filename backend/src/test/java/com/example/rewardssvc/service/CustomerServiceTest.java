package com.example.rewardssvc.service;

import com.example.rewardssvc.model.Customer;
import com.example.rewardssvc.model.Order;
import com.example.rewardssvc.repository.CustomerRepository;
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
public class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        String customer1Name = "alex";
        String customer2Name = "bob";
        Customer alex = Customer.builder().customerId(1L).name(customer1Name).build();
        Customer bob = Customer.builder().customerId(2L).name(customer2Name).build();

        Mockito.when(customerRepository.findByName(customer1Name))
                .thenReturn(List.of(alex));

        Mockito.when(customerRepository.findByName("rambo"))
                .thenReturn(emptyList());

        Mockito.when(customerRepository.findById(alex.getCustomerId()))
                .thenReturn(Optional.of(alex));
        doThrow(NoSuchElementException.class)
                .when(customerRepository).findById(71L);

        Mockito.when(customerRepository.findAll())
                .thenReturn(List.of(alex, bob));
    }

    @Test
    public void getAllCustomers_test() {
        List<Customer> allCustomers = customerService.getAllCustomers();

        assertNotNull(allCustomers);
        assertFalse(allCustomers.isEmpty());
        assertEquals(2, allCustomers.size());
        assertEquals("alex", allCustomers.get(0).getName());
        verify(customerRepository).findAll();
    }

    @Test
    public void getCustomerForCustomerId_test() {
        Long customerId = 1L;
        Customer customer = customerService.getCustomer(customerId);

        assertNotNull(customer);
        assertEquals(customerId, customer.getCustomerId());
        assertEquals("alex", customer.getName());
        verify(customerRepository).findById(customerId);
    }

    @Test
    public void getCustomersForCustomerIdAsNull_test() {
        Exception exception = assertThrows(NoSuchElementException.class, () -> customerService.getCustomer(71L));

        assertNotNull(exception);
        assertNull(exception.getLocalizedMessage());
        verify(customerRepository).findById(any());
    }

    @Test
    public void searchCustomersForCustomerName_test() {
        String customerName = "alex";
        List<Customer> allCustomers = customerService.searchCustomers(customerName);

        assertNotNull(allCustomers);
        assertFalse(allCustomers.isEmpty());
        assertEquals(1, allCustomers.size());
        assertEquals(customerName, allCustomers.get(0).getName());
        verify(customerRepository).findByName(customerName);
    }

    @Test
    public void searchCustomersForCustomerNameNotExist_test() {
        Exception exception = assertThrows(NoSuchElementException.class, () -> customerService.searchCustomers("rambo"));

        assertNotNull(exception);
        assertEquals("No customers found", exception.getLocalizedMessage());
        verify(customerRepository).findByName("rambo");
    }

    @Test
    public void searchCustomersForCustomerNameAsNull_test() {
        String customerName = "alex";
        List<Customer> allCustomers = customerService.searchCustomers(null);

        assertNotNull(allCustomers);
        assertFalse(allCustomers.isEmpty());
        assertEquals(2, allCustomers.size());
        assertEquals(customerName, allCustomers.get(0).getName());
        verify(customerRepository).findAll();
    }

    @Test
    public void saveCustomer_test() {

        Customer customer1 = Customer.builder().customerId(1L).build();
        Order order1 = Order.builder().orderId(1L).customer(customer1).build();
        Mockito.when(customerRepository.saveAndFlush(any())).thenReturn(customer1);

        Customer savedCustomer = customerService.save(Customer.builder().build());

        assertNotNull(savedCustomer);
        assertNotNull(savedCustomer.getCustomerId());
        assertEquals(1L, savedCustomer.getCustomerId());
        verify(customerRepository).saveAndFlush(any());
    }
}
