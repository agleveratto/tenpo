package com.agl.tenpo.domain.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SumServiceTest {

    private static final String PERCENTAGE_SERVICE_URL = "http://localhost:8080/api/v1/percentage/";
    private static final String HISTORY_SERVICE_URL = "http://localhost:8080/api/v1/history/";
    @InjectMocks
    SumServiceImpl sumService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RedisTemplate<String, Integer> redisTemplate;

    @Mock
    private ValueOperations<String, Integer> valueOperations;


    @BeforeEach
    void setup(){
        setReflections();
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    private void setReflections() {
        ReflectionTestUtils.setField(sumService, "PERCENTAGE_SERVICE_URL", PERCENTAGE_SERVICE_URL);
        ReflectionTestUtils.setField(sumService, "HISTORY_SERVICE_URL", HISTORY_SERVICE_URL);
    }

    @Test
    void sumNumbers(){
        when(restTemplate.getForObject(PERCENTAGE_SERVICE_URL, Integer.class)).thenReturn(81);
        assertThat(sumService.sum(20,23)).isEqualTo(77.83);

        verify(restTemplate).getForObject(PERCENTAGE_SERVICE_URL, Integer.class);
        verify(redisTemplate).opsForValue();
        verify(redisTemplate).expire("percentage", 1, TimeUnit.MINUTES);
    }

    @Test
    void getCachedPercentage(){
        when(redisTemplate.opsForValue().get("percentage")).thenReturn(1);

        assertThat(sumService.getCachedPercentage()).isEqualTo(1);

        verify(redisTemplate,times(2)).opsForValue();
    }
}
