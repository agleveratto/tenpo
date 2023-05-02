package com.agl.tenpo.infrastructure.limiters;

import com.agl.tenpo.domain.limiters.RpmLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SemaphoreRpmLimiter implements RpmLimiter {

    private final Map<String, Semaphore> ipSemaphores;

    public SemaphoreRpmLimiter() {
        this.ipSemaphores = new ConcurrentHashMap<>();
    }

    @Override
    public boolean allowRequest(String ipAddress) throws InterruptedException {
        log.info("date-time {}, validating requests per seconds for the ip {}", LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), ipAddress);
        Semaphore semaphore = ipSemaphores.computeIfAbsent(ipAddress, ip -> new Semaphore(3, true));
        return semaphore.tryAcquire(1, 1, TimeUnit.MINUTES);
    }

}