package com.example.rewardssvc.controller;

import com.example.rewardssvc.model.Rewards;
import com.example.rewardssvc.service.RewardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rewards Controller
 *
 * @author MKANAKAL
 */
@RestController
@RequestMapping(path = "api/v1/rewards")
public class RewardsController {

    private final RewardsService rewardsService;

    @Autowired
    public RewardsController(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }

    @GetMapping
    public Rewards getRewardsByCustomer(@RequestParam Long customerId) {
        return rewardsService.getRewardsByCustomer(customerId);
    }
}
