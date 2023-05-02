package com.agl.tenpo.application.controllers;

import com.agl.tenpo.application.services.SumService;
import com.agl.tenpo.infrastructure.limiters.SemaphoreRpmLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/v1/tenpo")
public class SumController {

    private final SumService sumService;
    private final SemaphoreRpmLimiter semaphoreRpmLimiter;

    public SumController(SumService sumService, SemaphoreRpmLimiter semaphoreRpmLimiter) {
        this.sumService = sumService;
        this.semaphoreRpmLimiter = semaphoreRpmLimiter;
    }

    @Operation(summary = "Sum two numbers and an external percentage value")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Result of the sum",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Double.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Percentage not exists into cache",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class))}),
            @ApiResponse(responseCode = "429",
                    description = "Too many requests per minute",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class))}),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error to get external percentage",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class))})
    })
    @GetMapping("/sum/{numberOne}/{numberTwo}")
    public ResponseEntity<Object> sumWithPercentage(@PathVariable int numberOne, @PathVariable int numberTwo,
                                                    HttpServletRequest httpServletRequest){
        try {
            if(!semaphoreRpmLimiter.allowRequest(httpServletRequest.getRemoteAddr())){
                return new ResponseEntity<>("Exceeded requests per minute", HttpStatus.TOO_MANY_REQUESTS);
            }
        } catch (InterruptedException e) {
            return new ResponseEntity<>("Fail trying to validate requests",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(sumService.sum(numberOne, numberTwo), HttpStatus.OK);
    }
}
