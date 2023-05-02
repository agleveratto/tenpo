package com.agl.tenpo.application.controllers;

import com.agl.tenpo.application.services.HistoryService;
import com.agl.tenpo.domain.entities.HistoryApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HistoryController.class)
public class HistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistoryService historyService;

    @Test
    void findAll_returnPageOfHistoryApi() throws Exception {
        HistoryApi historyApi = HistoryApi.builder()
                .id(1L)
                .executedDate(LocalDateTime.now().minusDays(2).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .num1(4)
                .num2(5)
                .percentage(10)
                .result(9.9)
                .statusCode(HttpStatus.CREATED.value())
                .build();

        HistoryApi historyApi2 = HistoryApi.builder()
                .id(2L)
                .executedDate(LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .num1(40)
                .num2(50)
                .percentage(10)
                .result(90.9)
                .statusCode(HttpStatus.CREATED.value())
                .build();

        Page<HistoryApi> historyApiPage = new PageImpl<>(List.of(historyApi, historyApi2));

        // Configurar el mock para que retorne el objeto Page cuando se llame al método findAll
        when(historyService.findAll(any())).thenReturn(historyApiPage);

        // Simular una petición HTTP GET a la URL "/api/v1/history"
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/history"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult).isNotNull();
        assertThat(mvcResult.getResponse()).isNotNull();
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(historyApiPage));

        verify(historyService).findAll(any());
    }

    @Test
    void save_thenReturnHttpStatus201() throws Exception {
        HistoryApi historyApi = HistoryApi.builder()
                .id(1L)
                .executedDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .num1(4)
                .num2(5)
                .percentage(10)
                .result(9.9)
                .statusCode(HttpStatus.CREATED.value())
                .build();

        doNothing().when(historyService).save(historyApi);

        // Simular una petición HTTP GET a la URL "/api/v1/history"
        mockMvc.perform(post("/api/v1/history")
                        .content(new ObjectMapper().writeValueAsString(historyApi))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        verify(historyService).save(historyApi);
    }
}
