package com.agl.tenpo.domain.services;

import com.agl.tenpo.domain.entities.HistoryApi;
import com.agl.tenpo.domain.exceptions.PercentageCachedNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        HistoryApi historyApi = HistoryApi.builder()
                .executedDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .num1(20)
                .num2(23)
                .percentage(81)
                .result(77.83)
                .statusCode(HttpStatus.CREATED.value())
                .build();

        when(restTemplate.getForObject(PERCENTAGE_SERVICE_URL, Integer.class)).thenReturn(81);
        when(restTemplate.postForEntity(HISTORY_SERVICE_URL, historyApi, HistoryApi.class)).thenReturn(new ResponseEntity<>(historyApi, HttpStatus.OK));

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

    @Test
    void getCachedPercentage_whenPercentageIsNotFoundIntoCache_thenThrowPercentageCachedNotFoundException(){
        when(redisTemplate.opsForValue().get("percentage")).thenReturn(null);

        assertThatThrownBy(() -> sumService.getCachedPercentage()).isInstanceOf(PercentageCachedNotFoundException.class);
        verify(redisTemplate,times(2)).opsForValue();
    }
}
