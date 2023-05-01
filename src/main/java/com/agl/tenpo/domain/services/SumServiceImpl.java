package com.agl.tenpo.domain.services;

import com.agl.tenpo.application.services.SumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class SumServiceImpl implements SumService {

    private final RestTemplate restTemplate;

    @Value("${percentage.service.url}")
    private String PERCENTAGE_SERVICE_URL;

    public SumServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public double sum(int num1, int num2) {
        int percentage = getExternalPercentage();
        log.info("percentage getted {}", percentage);
        int sum = num1 + num2;
        return sum + ((double) (percentage * sum) / 100);
    }

    private Integer getExternalPercentage() {
        log.info("getting percentage from external service");
        return restTemplate.getForObject(PERCENTAGE_SERVICE_URL, Integer.class);
    }
}
