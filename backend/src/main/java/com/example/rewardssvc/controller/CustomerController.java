package com.example.rewardssvc.controller;

import com.example.rewardssvc.model.Customer;
import com.example.rewardssvc.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Customer Controller
 *
 * @author MKANAKAL
 */
@RestController
@RequestMapping(path = "api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public Customer getCustomer(@RequestParam Long customerId) {
        return customerService.getCustomer(customerId);
    }

    @GetMapping(path = "/search")
    public List<Customer> searchCustomers(@RequestParam(required = false) String customerName) {
        return customerService.searchCustomers(customerName);
    }

    @PostMapping
    public Customer save(@RequestBody Customer customer) {
        return customerService.save(customer);
    }
}
