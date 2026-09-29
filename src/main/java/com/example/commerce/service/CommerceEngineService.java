package com.example.commerce.service;

import com.example.commerce.model.Product;
import com.example.commerce.model.PricingSuggestion;
import com.example.commerce.model.ReorderSuggestion;
import com.example.commerce.model.enums.BusinessTrigger;
import com.example.commerce.service.strategy.AIPricingStrategy;
import com.example.commerce.service.strategy.AIReorderStrategy;
import com.example.commerce.service.strategy.PricingStrategy;
import com.example.commerce.service.strategy.ReorderStrategy;
import com.example.commerce.service.strategy.RulePricingStrategy;
import com.example.commerce.service.strategy.RuleReorderStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CommerceEngineService {
    
    @Value("${commerce.pricing.strategy:rule}")
    private String pricingStrategy;
    
    @Value("${commerce.reorder.strategy:rule}")
    private String reorderStrategy;
    
    private final RulePricingStrategy rulePricingStrategy;
    private final AIPricingStrategy aiPricingStrategy;
    private final RuleReorderStrategy ruleReorderStrategy;
    private final AIReorderStrategy aiReorderStrategy;
    
    @Autowired
    public CommerceEngineService(RulePricingStrategy rulePricingStrategy, 
                                AIPricingStrategy aiPricingStrategy,
                                RuleReorderStrategy ruleReorderStrategy,
                                AIReorderStrategy aiReorderStrategy) {
        this.rulePricingStrategy = rulePricingStrategy;
        this.aiPricingStrategy = aiPricingStrategy;
        this.ruleReorderStrategy = ruleReorderStrategy;
        this.aiReorderStrategy = aiReorderStrategy;
    }
    
    public PricingSuggestion generatePricingRecommendation(Product product, BusinessTrigger trigger) {
        PricingStrategy strategy = getPricingStrategy();
        return strategy.generatePricingRecommendation(product, trigger);
    }
    
    public ReorderSuggestion generateReorderRecommendation(Product product, BusinessTrigger trigger) {
        ReorderStrategy strategy = getReorderStrategy();
        return strategy.generateReorderRecommendation(product, trigger);
    }
    
    private PricingStrategy getPricingStrategy() {
        switch (pricingStrategy.toLowerCase()) {
            case "ai":
                return aiPricingStrategy;
            case "rule":
            default:
                return rulePricingStrategy;
        }
    }
    
    private ReorderStrategy getReorderStrategy() {
        switch (reorderStrategy.toLowerCase()) {
            case "ai":
                return aiReorderStrategy;
            case "rule":
            default:
                return ruleReorderStrategy;
        }
    }
    
    // Methods to change strategy at runtime
    public void setPricingStrategy(String strategy) {
        this.pricingStrategy = strategy;
    }
    
    public void setReorderStrategy(String strategy) {
        this.reorderStrategy = strategy;
    }
}