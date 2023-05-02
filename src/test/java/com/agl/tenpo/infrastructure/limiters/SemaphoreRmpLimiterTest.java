package com.agl.tenpo.infrastructure.limiters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class SemaphoreRmpLimiterTest {

    private SemaphoreRpmLimiter semaphoreRpmLimiter;

    @BeforeEach
    void setup(){
        semaphoreRpmLimiter = new SemaphoreRpmLimiter();
    }

    @Test
    void allowRequest() throws InterruptedException {
        assertThat(semaphoreRpmLimiter.allowRequest("127.0.0.1")).isTrue();
    }
}
