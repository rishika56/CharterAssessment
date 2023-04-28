package com.example.rewardssvc.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Rewards {

    private Customer customer;
    private List<RewardEntry> monthlyRewards;
    private Double totalRewardPoints;
}
