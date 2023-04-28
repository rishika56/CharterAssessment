package com.example.rewardssvc.repository;

import com.example.rewardssvc.model.Customer;
import com.example.rewardssvc.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Order Repository
 *
 * @author MKANAKAL
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomer(Customer customer);
}
