package com.example.rewardssvc.service;

import com.example.rewardssvc.model.Customer;
import com.example.rewardssvc.model.Order;
import com.example.rewardssvc.model.Rewards;
import com.example.rewardssvc.repository.CustomerRepository;
import com.example.rewardssvc.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
public class RewardsServiceTest {

    @Autowired
    private RewardsService rewardsService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        Customer customer = Customer.builder().customerId(11L).name("alex").build();
        Order order1 = Order.builder()
                .orderId(1L)
                .customer(customer)
                .purchaseAmount(75.25)
                .purchaseDate(LocalDateTime.of(2022, Month.FEBRUARY, 1, 12, 30))
                .build();
        Order order2 = Order.builder()
                .orderId(2L)
                .customer(customer)
                .purchaseAmount(125.45)
                .purchaseDate(LocalDateTime.of(2022, Month.FEBRUARY, 1, 12, 30))
                .build();
        Order order3 = Order.builder()
                .orderId(3L)
                .customer(customer)
                .purchaseAmount(25.25)
                .purchaseDate(LocalDateTime.of(2022, Month.JANUARY, 1, 12, 30))
                .build();
        Order order4 = Order.builder()
                .orderId(4L)
                .customer(customer)
                .purchaseAmount(165.38)
                .purchaseDate(LocalDateTime.of(2022, Month.MARCH, 1, 12, 30))
                .build();

        Mockito.when(orderRepository.findByCustomer(customer))
                .thenReturn(List.of(order1, order2, order3, order4));

        Mockito.when(customerRepository.findById(11L))
                .thenReturn(Optional.of(customer));
    }

    @Test
    public void getRewardsByCustomer_test() {
        Rewards rewardsByCustomer = rewardsService.getRewardsByCustomer(11L);

        assertNotNull(rewardsByCustomer);
        assertFalse(rewardsByCustomer.getMonthlyRewards().isEmpty());
        assertEquals(3, rewardsByCustomer.getMonthlyRewards().size());
        assertEquals(306.90999999999997, rewardsByCustomer.getTotalRewardPoints());

        verify(orderRepository).findByCustomer(any());
    }

    @Test
    public void searchOrdersForOrderIdNotExists_test() {
        Exception exception = assertThrows(NoSuchElementException.class, () -> rewardsService.getRewardsByCustomer(12L));

        assertNotNull(exception);
        assertEquals("No customer found for the customer ID provided", exception.getLocalizedMessage());
        verify(customerRepository).findById(any());
    }
}
