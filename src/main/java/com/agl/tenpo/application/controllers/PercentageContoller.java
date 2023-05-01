package com.agl.tenpo.application.controllers;

import com.agl.tenpo.application.services.PercentageService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/percentage")
public class PercentageContoller {

    private final PercentageService percentageService;

    public PercentageContoller(PercentageService percentageService) {
        this.percentageService = percentageService;
    }

    @GetMapping("/")
    @Cacheable(value = "percentage")
    public ResponseEntity<Integer> retrievePercentage(){
        return new ResponseEntity<>(percentageService.retrievePercentage(), HttpStatus.OK);
    }

}
