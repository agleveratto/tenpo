package com.agl.tenpo.domain.services;

import com.agl.tenpo.application.services.SumService;
import org.springframework.stereotype.Service;

@Service
public class SumServiceImpl implements SumService {

    @Override
    public double sum(int num1, int num2) {
        return num1+num2;
    }
}
