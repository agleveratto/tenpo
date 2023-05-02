package com.agl.tenpo.domain.limiters;

public interface RpmLimiter {
     boolean allowRequest(String ipAddress) throws InterruptedException;
}
