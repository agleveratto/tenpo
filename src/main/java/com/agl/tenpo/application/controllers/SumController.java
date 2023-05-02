package com.agl.tenpo.application.controllers;

import com.agl.tenpo.application.services.SumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/tenpo")
public class SumController {

    private final SumService sumService;

    public SumController(SumService sumService) {
        this.sumService = sumService;
    }

    @GetMapping("/sum/{numberOne}/{numberTwo}")
    public ResponseEntity<Object> sumWithPercentage(@PathVariable int numberOne, @PathVariable int numberTwo) {
        return new ResponseEntity<>(sumService.sum(numberOne, numberTwo), HttpStatus.OK);
    }
}
