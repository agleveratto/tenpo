package com.agl.tenpo.domain.services;

import com.agl.tenpo.application.services.HistoryService;
import com.agl.tenpo.domain.entities.HistoryApi;
import com.agl.tenpo.infrastructure.repositories.PostgresRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final PostgresRepository postgresRepository;

    public HistoryServiceImpl(PostgresRepository postgresRepository) {
        this.postgresRepository = postgresRepository;
    }

    @Override
    public Page<HistoryApi> findAll(Pageable pageable){
        return postgresRepository.findAll(pageable);
    }

    @Override
    public void save(HistoryApi historyApi) {
        postgresRepository.save(historyApi);
    }
}
