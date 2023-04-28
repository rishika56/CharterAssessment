package com.example.rewardssvc.service;

import com.example.rewardssvc.model.Customer;
import com.example.rewardssvc.model.Order;
import com.example.rewardssvc.repository.CustomerRepository;
import com.example.rewardssvc.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Order service
 *
 * @author MKANAKAL
 */
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }

    public List<Order> searchOrders(Long customerId) {
        if (customerId != null) {
            Optional<Customer> customer = customerRepository.findById(customerId);
            if (customer.isEmpty()) {
                throw new NoSuchElementException("No customer found");
            }
            List<Order> orders = orderRepository.findByCustomer(customer.get());
            if (orders.isEmpty()) {
                throw new NoSuchElementException("No orders found");
            }
            return orders;
        } else {
            return getAllOrders();
        }
    }

    public Order save(Order order) {
        return orderRepository.saveAndFlush(order);
    }

}
