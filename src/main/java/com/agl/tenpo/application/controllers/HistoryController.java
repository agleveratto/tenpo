package com.agl.tenpo.application.controllers;

import com.agl.tenpo.application.services.HistoryService;
import com.agl.tenpo.domain.entities.HistoryApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/history")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @Operation(summary = "Retrieve a pageable of HistoryApi")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Pageable of HistoryApi",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pageable.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Not data into database",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pageable.class))}),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))})
    })
    @GetMapping()
    ResponseEntity<Page<HistoryApi>> findAll(@PageableDefault(sort = "id") Pageable pageable){
        return ResponseEntity.ok(historyService.findAll(pageable));
    }

    @Operation(summary = "Save a HistoryApi object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "HistoryApi object saved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class))}),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))})
    })
    @PostMapping()
    ResponseEntity<Object> save(@RequestBody HistoryApi historyApi){
        historyService.save(historyApi);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
