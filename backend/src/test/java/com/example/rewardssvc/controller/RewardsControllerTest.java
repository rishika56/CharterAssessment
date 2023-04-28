package com.example.rewardssvc.controller;

import com.example.rewardssvc.model.RewardEntry;
import com.example.rewardssvc.model.Rewards;
import com.example.rewardssvc.service.RewardsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

public class RewardsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RewardsService rewardsService;

    @BeforeEach
    public void setUp() {
        Mockito.when(rewardsService.getRewardsByCustomer(21L))
                .thenReturn(
                        Rewards.builder()
                                .monthlyRewards(
                                        List.of(
                                                RewardEntry.builder()
                                                        .month("JANUARY")
                                                        .year("2022")
                                                        .rewardPoints(50.25)
                                                        .build(),
                                                RewardEntry.builder()
                                                        .month("FEBRUARY")
                                                        .year("2022")
                                                        .rewardPoints(25.45)
                                                        .build())
                                )
                                .totalRewardPoints(125.56)
                                .build());
        doThrow(NoSuchElementException.class)
                .when(rewardsService).getRewardsByCustomer(22L);
    }

    @Test
    public void givenRewards_whenGetRewardsByCustomerId_thenStatus200()
            throws Exception {

        mvc.perform(get("/api/v1/rewards?customerId=21")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalRewardPoints", is(125.56)))
                .andExpect(jsonPath("$.monthlyRewards[0].year", is("2022")))
                .andExpect(jsonPath("$.monthlyRewards.length()", is(2)));
    }

    @Test
    public void givenRewards_whenGetRewardsByCustomerId_thenStatusNotFound()
            throws Exception {

        mvc.perform(get("/api/v1/rewards?customerId=22")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("No data found")));
    }

}
