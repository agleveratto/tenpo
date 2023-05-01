package com.agl.tenpo.domain.services;

import com.agl.tenpo.application.services.PercentageService;
import org.springframework.stereotype.Service;

@Service
public class PercentageServiceImpl implements PercentageService {
    @Override
    public int retrievePercentage() {
        return (int) (Math.random()*100+1);
    }
}
