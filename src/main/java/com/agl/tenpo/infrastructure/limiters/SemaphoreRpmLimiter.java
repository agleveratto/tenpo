package com.agl.tenpo.infrastructure.limiters;

import com.agl.tenpo.domain.limiters.RpmLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class SemaphoreRpmLimiter implements RpmLimiter {
    private final ConcurrentHashMap<String, AtomicInteger> ipRequestCount = new ConcurrentHashMap<>();
    private long startTime = System.currentTimeMillis();

    @Override
    public boolean allowRequest(String ipAddress) throws InterruptedException {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;

        if (elapsedTime >= 60000) { // 60 segundos en milisegundos
            startTime = currentTime;
            ipRequestCount.clear();
        }

        AtomicInteger requestCount = ipRequestCount.get(ipAddress);
        if (requestCount == null) {
            requestCount = new AtomicInteger(0);
            ipRequestCount.put(ipAddress, requestCount);
        }

        requestCount.incrementAndGet();
        return requestCount.get() <= 3;
    }
}