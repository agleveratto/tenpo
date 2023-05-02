package com.agl.tenpo.application.services;

import com.agl.tenpo.domain.entities.HistoryApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistoryService {

    Page<HistoryApi> findAll(Pageable pageable);
    void save(HistoryApi historyApi);
}
