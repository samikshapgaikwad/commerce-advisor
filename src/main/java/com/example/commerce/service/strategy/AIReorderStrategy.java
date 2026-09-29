package com.example.commerce.service.strategy;

import com.example.commerce.model.Product;
import com.example.commerce.model.ReorderSuggestion;
import com.example.commerce.model.enums.BusinessTrigger;
import com.example.commerce.service.AiAdvisorService;
import com.example.commerce.service.ai.model.AiReorderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AIReorderStrategy implements ReorderStrategy {
    
    private final AiAdvisorService aiAdvisorService;
    private final RuleReorderStrategy ruleReorderStrategy;
    
    @Autowired
    public AIReorderStrategy(AiAdvisorService aiAdvisorService, RuleReorderStrategy ruleReorderStrategy) {
        this.aiAdvisorService = aiAdvisorService;
        this.ruleReorderStrategy = ruleReorderStrategy;
    }
    
    @Override
    public ReorderSuggestion generateReorderRecommendation(Product product, BusinessTrigger trigger) {
        try {
            AiReorderResponse aiResponse = aiAdvisorService.generateReorderRecommendation(product, trigger);
            
            // Validate AI response
            if (aiResponse.getRecommendedQuantity() <= 0) {
                throw new IllegalArgumentException("Invalid recommended quantity: " + aiResponse.getRecommendedQuantity());
            }
            
            if (aiResponse.getConfidence() < 0 || aiResponse.getConfidence() > 1) {
                throw new IllegalArgumentException("Invalid confidence value: " + aiResponse.getConfidence());
            }
            
            if (aiResponse.getReasoning() == null || aiResponse.getReasoning().trim().isEmpty()) {
                throw new IllegalArgumentException("Empty reasoning");
            }
            
            return new ReorderSuggestion(
                product,
                trigger,
                aiResponse.getRecommendedQuantity(),
                aiResponse.getConfidence(),
                aiResponse.getReasoning()
            );
        } catch (Exception e) {
            // Fallback to rule-based strategy
            return ruleReorderStrategy.generateReorderRecommendation(product, trigger);
        }
    }
}