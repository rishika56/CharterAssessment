package com.example.rewardssvc.service;

import com.example.rewardssvc.controller.OrderController;
import com.example.rewardssvc.model.Customer;
import com.example.rewardssvc.repository.CustomerRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Customer service
 *
 * @author MKANAKAL
 */
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();

        // Add link for orders by customer id
        customers.forEach(customer -> customer.add(getOrdersByCustomerIdLink(customer.getCustomerId())));
        return customers;
    }

    public Customer getCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow();

        // Add link for orders by customer id
        customer.add(getOrdersByCustomerIdLink(customer.getCustomerId()));
        return customer;
    }

    public List<Customer> searchCustomers(String customerName) {
        if (StringUtils.isNotEmpty(customerName)) {
            List<Customer> customers = customerRepository.findByName(customerName);

            if (customers.isEmpty()) {
                throw new NoSuchElementException("No customers found");
            }

            // Add link for orders by customer id
            customers.forEach(customer -> customer.add(getOrdersByCustomerIdLink(customer.getCustomerId())));

            return customers;
        } else {
            return getAllCustomers();
        }
    }

    public Customer save(Customer customer) {
        return customerRepository.saveAndFlush(customer);
    }

    /**
     * Create link for customer orders by customer id
     * @param customerId for which orders to be fetched
     * @return link for customer orders
     */
    private Link getOrdersByCustomerIdLink(Long customerId){
        return linkTo(methodOn(OrderController.class)
                .searchOrders(customerId)).withSelfRel();
    }
}
