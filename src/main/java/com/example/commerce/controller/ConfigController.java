package com.example.commerce.controller;

import com.example.commerce.service.CommerceEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:4200"})
public class ConfigController {
    
    private final CommerceEngineService commerceEngineService;
    
    @Autowired
    public ConfigController(CommerceEngineService commerceEngineService) {
        this.commerceEngineService = commerceEngineService;
    }
    
    @PostMapping("/pricing-strategy/{strategy}")
    public void setPricingStrategy(@PathVariable String strategy) {
        commerceEngineService.setPricingStrategy(strategy);
    }
    
    @PostMapping("/reorder-strategy/{strategy}")
    public void setReorderStrategy(@PathVariable String strategy) {
        commerceEngineService.setReorderStrategy(strategy);
    }
}