package com.agl.tenpo.application.controllers;

import com.agl.tenpo.application.services.HistoryService;
import com.agl.tenpo.domain.entities.HistoryApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/history")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping()
    ResponseEntity<Page<HistoryApi>> findAll(@PageableDefault(sort = "id") Pageable pageable){
        return ResponseEntity.ok(historyService.findAll(pageable));
    }

    @PostMapping()
    ResponseEntity<Object> save(@RequestBody HistoryApi historyApi){
        historyService.save(historyApi);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
