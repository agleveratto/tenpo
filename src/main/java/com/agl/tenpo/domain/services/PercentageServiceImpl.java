package com.agl.tenpo.domain.services;

import com.agl.tenpo.application.services.PercentageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@CacheConfig(cacheNames = "percentage")
public class PercentageServiceImpl implements PercentageService {
    @Override

    @Cacheable
    public int retrievePercentage() {
        return (int) (Math.random()*100+1);
    }

    @Scheduled(fixedRate = 1800000)
    @CacheEvict(allEntries = true)
    public void clearCache(){
        log.info("clearing cache");
    }
}
