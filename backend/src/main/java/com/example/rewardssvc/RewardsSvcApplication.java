package com.example.rewardssvc;

import com.example.rewardssvc.model.Customer;
import com.example.rewardssvc.model.Order;
import com.example.rewardssvc.repository.CustomerRepository;
import com.example.rewardssvc.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Month;

@SpringBootApplication
@Slf4j
public class RewardsSvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(RewardsSvcApplication.class, args);
    }

    @Component
    @Profile("!test")
    static
    class RewardsCommandLineRunner implements CommandLineRunner {
        private final CustomerRepository customerRepository;
        private final OrderRepository orderRepository;

        @Autowired
        public RewardsCommandLineRunner(CustomerRepository customerRepository, OrderRepository orderRepository) {
            this.customerRepository = customerRepository;
            this.orderRepository = orderRepository;
        }

        @Override
        public void run(String... args) {

            log.info("load data to h2 - started");

            Customer johnDoe = customerRepository.save(Customer.builder().name("John Doe").email("john.doe@email.com").build());
            Customer janeDoe = customerRepository.save(Customer.builder().name("Jane Doe").email("jane.doe@email.com").build());
            Customer joeBlow = customerRepository.save(Customer.builder().name("Joe Blow").email("joe.blow@email.com").build());

            orderRepository.save(Order.builder().customer(johnDoe).purchaseDate(LocalDateTime.of(2022, Month.APRIL, 22, 12, 30)).purchaseAmount(75.56).build());
            orderRepository.save(Order.builder().customer(johnDoe).purchaseDate(LocalDateTime.of(2022, Month.APRIL, 22, 12, 30)).purchaseAmount(25.90).build());
            orderRepository.save(Order.builder().customer(johnDoe).purchaseDate(LocalDateTime.of(2021, Month.AUGUST, 22, 12, 30)).purchaseAmount(125.36).build());
            orderRepository.save(Order.builder().customer(johnDoe).purchaseDate(LocalDateTime.of(2022, Month.FEBRUARY, 22, 12, 30)).purchaseAmount(174.36).build());
            orderRepository.save(Order.builder().customer(janeDoe).purchaseDate(LocalDateTime.of(2022, Month.JANUARY, 22, 12, 30)).purchaseAmount(68.98).build());
            orderRepository.save(Order.builder().customer(janeDoe).purchaseDate(LocalDateTime.of(2022, Month.MARCH, 22, 12, 30)).purchaseAmount(75.24).build());
            orderRepository.save(Order.builder().customer(janeDoe).purchaseDate(LocalDateTime.of(2022, Month.FEBRUARY, 22, 12, 30)).purchaseAmount(175.56).build());
            orderRepository.save(Order.builder().customer(joeBlow).purchaseDate(LocalDateTime.of(2022, Month.JANUARY, 22, 12, 30)).purchaseAmount(15.56).build());

            log.info("load data to h2 - complete");
        }

    }

}
