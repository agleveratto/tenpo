package com.agl.tenpo.application.controllers;

import com.agl.tenpo.application.services.SumService;
import com.agl.tenpo.domain.exceptions.PercentageCachedNotFoundException;
import com.agl.tenpo.infrastructure.limiters.SemaphoreRpmLimiter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SumController.class)
class SumControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SumService tenpoService;
    @MockBean
    private SemaphoreRpmLimiter semaphoreRpmLimiter;

    @Test
    public void sumNumbers_thenReturn200() throws Exception {
        // Configuro el mock para permitir acceso con mi IP
        when(semaphoreRpmLimiter.allowRequest("127.0.0.1")).thenReturn(true);

        when(tenpoService.sum(4,5)).thenReturn(10.0);

        // Hago 3 consultas al controlador, esperando tener status OK
        for (int i = 0 ; i < 3 ; i++){
            mockMvc.perform(get("/api/v1/tenpo/sum/{numberOne}/{numberTwo}", 4, 5)).andExpect(status().isOk());
        }
        // Configuro el mock para NO permitir acceso con mi IP
        when(semaphoreRpmLimiter.allowRequest("127.0.0.1")).thenReturn(false);

        // Hacer una consulta esperando tener status TOO MANY REQUESTS
        mockMvc.perform(get("/api/v1/tenpo/sum/{numberOne}/{numberTwo}", 4, 5)).andExpect(status().isTooManyRequests());

        // Verificar que se permitieron o denegaron las solicitudes según lo esperado
        verify(semaphoreRpmLimiter, times(4)).allowRequest("127.0.0.1");
        verify(tenpoService, times(3)).sum(4,5);
    }

    @Test
    public void sumNumbers_thenReturn404() throws Exception {
        // Configuro el mock para permitir acceso con mi IP
        when(semaphoreRpmLimiter.allowRequest("127.0.0.1")).thenReturn(true);

        when(tenpoService.sum(1,2)).thenThrow(PercentageCachedNotFoundException.class);

        var result = mockMvc.perform(get("/api/v1/tenpo/sum/{numberOne}/{numberTwo}", 1, 2))
                .andExpect(status().isNotFound())
                .andReturn();

        var resultMapped = new ObjectMapper().readValue(result.getResponse().getContentAsString(), HashMap.class);
        assertThat(resultMapped.get("message")).isEqualTo("Percentage not found into cache");

        verify(semaphoreRpmLimiter).allowRequest("127.0.0.1");
        verify(tenpoService).sum(1,2);
    }

    @Test
    public void sumNumbers_thenReturn429() throws Exception {
        // Configuro el mock para permitir acceso con mi IP
        when(semaphoreRpmLimiter.allowRequest("127.0.0.1")).thenReturn(true);

        when(tenpoService.sum(4,5)).thenReturn(10.0);

        // Hago 3 consultas al controlador, esperando tener status OK
        for (int i = 0 ; i < 3 ; i++){
            mockMvc.perform(get("/api/v1/tenpo/sum/{numberOne}/{numberTwo}", 4, 5)).andExpect(status().isOk());
        }
        // Configuro el mock para NO permitir acceso con mi IP
        when(semaphoreRpmLimiter.allowRequest("127.0.0.1")).thenReturn(false);

        // Hacer una consulta esperando tener status TOO MANY REQUESTS
        var result = mockMvc.perform(get("/api/v1/tenpo/sum/{numberOne}/{numberTwo}", 4, 5)).andExpect(status().isTooManyRequests()).andReturn();

        assertThat(result).isNotNull();
        assertThat(result.getResponse()).isNotNull();
        assertThat(result.getResponse().getContentAsString()).isEqualTo("Exceeded requests per minute");

        // Verificar que se permitieron o denegaron las solicitudes según lo esperado
        verify(semaphoreRpmLimiter, times(4)).allowRequest("127.0.0.1");
        verify(tenpoService, times(3)).sum(4,5);
    }

    @Test
    public void sumNumbers_whenFailsValidatingRequests_thenReturn500() throws Exception {
        // Configuro el mock para permitir acceso con mi IP
        when(semaphoreRpmLimiter.allowRequest("127.0.0.1")).thenThrow(InterruptedException.class);

        var result = mockMvc.perform(get("/api/v1/tenpo/sum/{numberOne}/{numberTwo}", 1, 2))
                .andExpect(status().isInternalServerError())
                .andReturn();

        assertThat(result).isNotNull();
        assertThat(result.getResponse()).isNotNull();
        assertThat(result.getResponse().getContentAsString()).isEqualTo("Fail trying to validate requests");

        verify(semaphoreRpmLimiter).allowRequest("127.0.0.1");
        verify(tenpoService, never()).sum(1,2);
    }

    @Test
    public void sumNumbers_thenReturn500() throws Exception {
        // Configuro el mock para permitir acceso con mi IP
        when(semaphoreRpmLimiter.allowRequest("127.0.0.1")).thenReturn(true);

        when(tenpoService.sum(1,2)).thenThrow(RestClientException.class);

        var result = mockMvc.perform(get("/api/v1/tenpo/sum/{numberOne}/{numberTwo}", 1, 2))
                .andExpect(status().isInternalServerError())
                .andReturn();

        var resultMapped = new ObjectMapper().readValue(result.getResponse().getContentAsString(), HashMap.class);
        assertThat(resultMapped.get("message")).isEqualTo("Internal Server Error");

        verify(semaphoreRpmLimiter).allowRequest("127.0.0.1");
        verify(tenpoService).sum(1,2);
    }

}