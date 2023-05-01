package com.agl.tenpo.domain.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SumServiceTest {

    private static final String PERCENTAGE_SERVICE_URL = "http://localhost:8080/api/v1/percentage/";
    @InjectMocks
    SumServiceImpl sumService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setup(){
        setReflections();
    }

    private void setReflections() {
        ReflectionTestUtils.setField(sumService, "PERCENTAGE_SERVICE_URL", PERCENTAGE_SERVICE_URL);
    }

    @Test
    void sumNumbers(){
        when(restTemplate.getForObject(PERCENTAGE_SERVICE_URL, Integer.class)).thenReturn(81);

        assertThat(sumService.sum(20,23)).isEqualTo(77.83);

        verify(restTemplate).getForObject(PERCENTAGE_SERVICE_URL, Integer.class);
    }
}
