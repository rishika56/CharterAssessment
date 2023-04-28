package com.example.rewardssvc.service;

import com.example.rewardssvc.model.Customer;
import com.example.rewardssvc.model.Order;
import com.example.rewardssvc.model.RewardEntry;
import com.example.rewardssvc.model.Rewards;
import com.example.rewardssvc.repository.CustomerRepository;
import com.example.rewardssvc.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.util.stream.Collectors.*;

/**
 * Order service
 *
 * @author MKANAKAL
 */
@Service
@Slf4j
public class RewardsService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public RewardsService(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    public Rewards getRewardsByCustomer(Long customerId) {

        Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isEmpty()) {
            throw new NoSuchElementException("No customer found for the customer ID provided");
        }

        List<Order> ordersByCustomerId = orderRepository.findByCustomer(customer.get());

        if (ordersByCustomerId.isEmpty()) {
            throw new NoSuchElementException("No orders found for the customer ID provided");
        }

        // Group orders by month and calculate reward points
        Map<YearMonth, Double> rewardsByYearMonth = ordersByCustomerId.stream()
                .collect(groupingBy(m -> YearMonth.from(m.getPurchaseDate()),
                        summingDouble(m -> getRewardPoints(m.getPurchaseAmount()))));

        return Rewards.builder()
                .customer(customer.get())
                .monthlyRewards(getRewardEntries(rewardsByYearMonth))
                .totalRewardPoints(getTotalRewardPoints(rewardsByYearMonth))
                .build();
    }

    private Double getTotalRewardPoints(Map<YearMonth, Double> rewardsByYearMonth) {
        return rewardsByYearMonth.values().stream().mapToDouble(d -> d).sum();
    }

    /**
     * Prepare reward entry list
     *
     * @param rewardsByYearMonth map of rewards grouped by month
     * @return list of reward entries
     */
    private List<RewardEntry> getRewardEntries(Map<YearMonth, Double> rewardsByYearMonth) {
        return rewardsByYearMonth.entrySet().stream()
                .map(e -> RewardEntry.builder()
                        .month(e.getKey().getMonth().name())
                        .year(String.valueOf(e.getKey().getYear()))
                        .rewardPoints(e.getValue()).build())
                .collect(toList());
    }

    /**
     * Calculate awards points from the purchase amount. Don't exclude cents/decimal part while calculating.
     * A customer receives 2 points for every dollar spent over $100 in each transaction, plus 1 point for every dollar spent over $50 in each transaction
     * (e.g. a $120 purchase = 2x$20 + 1x$50 = 90 points).
     *
     * @param purchaseAmount actual order value
     * @return reward points
     */
    private double getRewardPoints(double purchaseAmount) {
        log.debug("purchaseAmount: " + purchaseAmount);
        double rewardPoints = 0;
        if (purchaseAmount > 100) {
            rewardPoints += purchaseAmount - 100;
        }
        if (purchaseAmount > 50) {
            rewardPoints += purchaseAmount - 50;
        }
        log.debug("rewardPoints: " + rewardPoints);
        return rewardPoints;
    }

}
