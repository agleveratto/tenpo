package com.agl.tenpo.application.controllers;

import com.agl.tenpo.application.services.SumService;
import com.agl.tenpo.infrastructure.limiters.SemaphoreRpmLimiter;
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

    @GetMapping("/sum/{numberOne}/{numberTwo}")
    public ResponseEntity<Object> sumWithPercentage(@PathVariable int numberOne, @PathVariable int numberTwo,
                                                    HttpServletRequest httpServletRequest){
        try {
            if(!semaphoreRpmLimiter.allowRequest(httpServletRequest.getRemoteAddr())){
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null);
            }
        } catch (InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return new ResponseEntity<>(sumService.sum(numberOne, numberTwo), HttpStatus.OK);
    }
}
