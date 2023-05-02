package com.agl.tenpo.infrastructure.repositories;

import com.agl.tenpo.domain.entities.HistoryApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostgresRepository extends JpaRepository<HistoryApi, Long> {
}
