package com.agl.tenpo.domain.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PercentageServiceTest {

    @InjectMocks
    PercentageServiceImpl percentageService;

    @Test
    void retrievePercentage(){
        assertThat(percentageService.retrievePercentage()).isNotNull().isNotNegative();
    }
}
