package com.example.commerce.service.strategy;

import com.example.commerce.model.Product;
import com.example.commerce.model.PricingSuggestion;
import com.example.commerce.model.enums.BusinessTrigger;
import com.example.commerce.model.enums.PriceDirection;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class RulePricingStrategy implements PricingStrategy {

    @Override
    public PricingSuggestion generatePricingRecommendation(Product product, BusinessTrigger trigger) {
        BigDecimal recommendedPrice = product.getCurrentPrice();
        PriceDirection direction = PriceDirection.MAINTAIN;
        String reasoning = "Default rule-based pricing recommendation";
        double confidence = 0.7;

        switch (trigger) {
            case INVENTORY_LOW:
                // For low inventory, consider increasing price to reduce demand
                // or decreasing price to clear inventory quickly
                if (product.getDemandVelocity() > 10) {
                    // High demand product - increase price
                    recommendedPrice = product.getCurrentPrice().multiply(BigDecimal.valueOf(1.1)); // 10% increase
                    direction = PriceDirection.INCREASE;
                    reasoning = "Low stock with high demand velocity suggests increasing price to balance supply and demand";
                    confidence = 0.8;
                } else {
                    // Lower demand - maintain or slight decrease
                    recommendedPrice = product.getCurrentPrice().multiply(BigDecimal.valueOf(0.95)); // 5% decrease
                    direction = PriceDirection.DECREASE;
                    reasoning = "Low stock with moderate demand suggests slight price reduction to stimulate sales";
                    confidence = 0.6;
                }
                break;
                
            case DEMAND_SPIKE:
                // For demand spike, consider increasing price to capitalize
                recommendedPrice = product.getCurrentPrice().multiply(BigDecimal.valueOf(1.15)); // 15% increase
                direction = PriceDirection.INCREASE;
                reasoning = "High demand velocity spike suggests increasing price to optimize revenue";
                confidence = 0.85;
                break;
        }

        // Round to 2 decimal places
        recommendedPrice = recommendedPrice.setScale(2, RoundingMode.HALF_UP);

        return new PricingSuggestion(product, trigger, recommendedPrice, direction, confidence, reasoning);
    }
}