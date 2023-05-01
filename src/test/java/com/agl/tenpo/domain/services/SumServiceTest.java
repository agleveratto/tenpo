package com.agl.tenpo.domain.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class SumServiceTest {

    @InjectMocks
    SumServiceImpl sumService;

    @Test
    void sumNumbers(){
        double result = sumService.sum(1, 2);

        assertThat(result).isEqualTo(3.0);
    }
}
