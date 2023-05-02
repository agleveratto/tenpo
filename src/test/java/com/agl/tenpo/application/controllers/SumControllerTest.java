package com.agl.tenpo.application.controllers;

import com.agl.tenpo.application.services.SumService;
import com.agl.tenpo.domain.exceptions.PercentageCachedNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SumController.class)
class SumControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SumService tenpoService;

    @Test
    public void sumNumbers_thenReturn200() throws Exception {
        when(tenpoService.sum(1,2)).thenReturn(3.0);

        MvcResult result = mockMvc.perform(get("/api/v1/tenpo/sum/{numberOne}/{numberTwo}", 1, 2))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("3.0");

        verify(tenpoService).sum(1,2);
    }

    @Test
    public void sumNumbers_thenReturn404() throws Exception {
        when(tenpoService.sum(1,2)).thenThrow(PercentageCachedNotFoundException.class);

        MvcResult result = mockMvc.perform(get("/api/v1/tenpo/sum/{numberOne}/{numberTwo}", 1, 2))
                .andExpect(status().isNotFound())
                .andReturn();

        HashMap resultMapped = new ObjectMapper().readValue(result.getResponse().getContentAsString(), HashMap.class);
        assertThat(resultMapped.get("message")).isEqualTo("Percentage not found into cache");

        verify(tenpoService).sum(1,2);
    }

    @Test
    public void sumNumbers_thenReturn500() throws Exception {
        when(tenpoService.sum(1,2)).thenThrow(RestClientException.class);

        MvcResult result = mockMvc.perform(get("/api/v1/tenpo/sum/{numberOne}/{numberTwo}", 1, 2))
                .andExpect(status().isInternalServerError())
                .andReturn();

        HashMap resultMapped = new ObjectMapper().readValue(result.getResponse().getContentAsString(), HashMap.class);
        assertThat(resultMapped.get("message")).isEqualTo("Internal Server Error");


        verify(tenpoService).sum(1,2);
    }

}