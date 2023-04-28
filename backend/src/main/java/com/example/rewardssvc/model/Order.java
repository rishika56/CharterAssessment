package com.example.rewardssvc.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderId;
    private LocalDateTime purchaseDate;
    private Double purchaseAmount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Customer customer;
}
