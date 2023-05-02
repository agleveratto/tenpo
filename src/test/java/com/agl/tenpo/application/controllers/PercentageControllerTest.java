package com.agl.tenpo.application.controllers;

import com.agl.tenpo.application.services.PercentageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PercentageController.class)
public class PercentageControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PercentageService percentageService;

    @Test
    void retrievePercentage() throws Exception {
        when(percentageService.retrievePercentage()).thenReturn(5);

        MvcResult result = mockMvc.perform(get("/api/v1/percentage/"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("5");

        verify(percentageService).retrievePercentage();


    }
}
