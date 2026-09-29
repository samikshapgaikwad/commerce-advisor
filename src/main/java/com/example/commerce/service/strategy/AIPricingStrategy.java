package com.example.commerce.service.strategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.commerce.model.Product;
import com.example.commerce.model.PricingSuggestion;
import com.example.commerce.model.enums.BusinessTrigger;
import com.example.commerce.model.enums.PriceDirection;
import com.example.commerce.service.AiAdvisorService;
import com.example.commerce.service.ai.model.AiPricingResponse;


@Service
public class AIPricingStrategy implements PricingStrategy {
    
    private final AiAdvisorService aiAdvisorService;
    private final RulePricingStrategy rulePricingStrategy;
    
    @Autowired
    public AIPricingStrategy(AiAdvisorService aiAdvisorService, RulePricingStrategy rulePricingStrategy) {
        this.aiAdvisorService = aiAdvisorService;
        this.rulePricingStrategy = rulePricingStrategy;
    }
    
    @Override
    public PricingSuggestion generatePricingRecommendation(Product product, BusinessTrigger trigger) {
        try {
            AiPricingResponse aiResponse = aiAdvisorService.generatePricingRecommendation(product, trigger);
            
            // Validate AI response
            if (aiResponse.getRecommendedPrice() <= 0) {
                throw new IllegalArgumentException("Invalid recommended price: " + aiResponse.getRecommendedPrice());
            }
            
            if (aiResponse.getConfidence() < 0 || aiResponse.getConfidence() > 1) {
                throw new IllegalArgumentException("Invalid confidence value: " + aiResponse.getConfidence());
            }
            
            if (aiResponse.getReasoning() == null || aiResponse.getReasoning().trim().isEmpty()) {
                throw new IllegalArgumentException("Empty reasoning");
            }
            
            PriceDirection direction;
            try {
                direction = PriceDirection.valueOf(aiResponse.getDirection().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid direction: " + aiResponse.getDirection());
            }
            
            return new PricingSuggestion(
                product,
                trigger,
                java.math.BigDecimal.valueOf(aiResponse.getRecommendedPrice()),
                direction,
                aiResponse.getConfidence(),
                aiResponse.getReasoning()
            );
        } catch (Exception e) {
            // Fallback to rule-based strategy
            return rulePricingStrategy.generatePricingRecommendation(product, trigger);
        }
    }
}