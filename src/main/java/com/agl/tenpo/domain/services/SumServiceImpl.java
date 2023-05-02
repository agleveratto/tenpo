package com.agl.tenpo.domain.services;

import com.agl.tenpo.application.services.SumService;
import com.agl.tenpo.domain.entities.HistoryApi;
import com.agl.tenpo.domain.exceptions.PercentageCachedNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SumServiceImpl implements SumService {

    private static final String PERCENTAGE_KEY = "percentage";
    private final RestTemplate restTemplate;
    private final RedisTemplate<String, Integer> redisTemplate;

    @Value("${percentage.service.url}")
    private String PERCENTAGE_SERVICE_URL;

    @Value("${history.service.url}")
    private String HISTORY_SERVICE_URL;

    public SumServiceImpl(RestTemplate restTemplate, RedisTemplate<String, Integer> redisTemplate) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public double sum(int numberOne, int numberTwo) {
        var localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        var percentage = getExternalPercentage();
        log.info("percentage getted {}", percentage);
        var sum = numberOne + numberTwo;
        var result = sum + ((double) (percentage * sum) / 100);
        saveData(localDateTime, percentage, numberOne, numberTwo, result);
        return result;
    }

    @Retryable()
    public Integer getExternalPercentage() {
        log.info("getting percentage from external service {}", PERCENTAGE_SERVICE_URL);
        var externalPercentage = restTemplate.getForObject(PERCENTAGE_SERVICE_URL, Integer.class);
        redisTemplate.opsForValue().set(PERCENTAGE_KEY, externalPercentage);
        redisTemplate.expire(PERCENTAGE_KEY,30, TimeUnit.MINUTES);
        return externalPercentage;
    }

    @Recover
    public Integer getCachedPercentage(){
        var cachedPercentage = redisTemplate.opsForValue().get(PERCENTAGE_KEY);
        if (cachedPercentage == null) {
            throw new PercentageCachedNotFoundException();
        }
        return cachedPercentage;
    }

    @Async
    void saveData(String localDateTime, int percentage, int numberOne, int numberTwo, Double result){
        log.info("save data into historyApi");
        var historyApi = HistoryApi.builder()
                .executedDate(localDateTime)
                .num1(numberOne)
                .num2(numberTwo)
                .percentage(percentage)
                .result(result)
                .statusCode(HttpStatus.CREATED.value())
                .build();
        try {
            restTemplate.postForEntity(HISTORY_SERVICE_URL, historyApi, HistoryApi.class);
        } catch (RestClientException e) {
            log.info("error saving history object.");
        }
    }

}
