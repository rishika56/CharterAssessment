package com.example.rewardssvc.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RewardEntry {
    private String month;
    private String year;
    private Double rewardPoints;
}
