package com.agl.tenpo.domain.services;

import com.agl.tenpo.domain.entities.HistoryApi;
import com.agl.tenpo.infrastructure.repositories.PostgresRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HistoryServiceTest {

    @InjectMocks
    HistoryServiceImpl historyService;

    @Mock
    PostgresRepository postgresRepository;

    @Test
    void findAll_returnHistoryApiPageable(){
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

        List<HistoryApi> historyApiList = List.of(historyApi, historyApi2);
        Page<HistoryApi> historyApiPage = new PageImpl<>(historyApiList);

        when(postgresRepository.findAll(Pageable.unpaged())).thenReturn(historyApiPage);

        Page<HistoryApi> result = historyService.findAll(Pageable.unpaged());

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getContent())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(historyApiList);

        verify(postgresRepository).findAll(Pageable.unpaged());
    }

    @Test
    void save_givenHistoryApi_thenSaveInPostgres(){
        HistoryApi historyApi = HistoryApi.builder()
                .id(1L)
                .executedDate(LocalDateTime.now().minusDays(2).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .num1(4)
                .num2(5)
                .percentage(10)
                .result(9.9)
                .statusCode(HttpStatus.CREATED.value())
                .build();

        when(postgresRepository.save(historyApi)).thenReturn(historyApi);

        historyService.save(historyApi);

        verify(postgresRepository).save(historyApi);
    }
}
