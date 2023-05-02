package com.agl.tenpo.domain.services;

import com.agl.tenpo.application.services.SumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SumServiceImpl implements SumService {

    private static final String PERCENTAGE_KEY = "percentage";
    private final RestTemplate restTemplate;
    private final RedisTemplate<String, Integer> redisTemplate;

    @Value("${percentage.service.url}")
    private String PERCENTAGE_SERVICE_URL;

    public SumServiceImpl(RestTemplate restTemplate, RedisTemplate<String, Integer> redisTemplate) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public double sum(int num1, int num2) {
        int percentage = getExternalPercentage();
        log.info("percentage getted {}", percentage);
        int sum = num1 + num2;
        return sum + ((double) (percentage * sum) / 100);
    }

    @Retryable()
    public Integer getExternalPercentage() {
        log.info("getting percentage from external service");
        Integer externalPercentage = restTemplate.getForObject(PERCENTAGE_SERVICE_URL, Integer.class);
        redisTemplate.opsForValue().set(PERCENTAGE_KEY, externalPercentage);
        redisTemplate.expire(PERCENTAGE_KEY, 1, TimeUnit.MINUTES);
        return externalPercentage;
    }

    @Recover
    public Integer getCachedPercentage(){
        Integer cachedPercentage = redisTemplate.opsForValue().get(PERCENTAGE_KEY);
        if (cachedPercentage == null) {
            //todo throw custom exception
        }
        return cachedPercentage;
    }

}
