package com.example.commerce.service.strategy;

import com.example.commerce.model.Product;
import com.example.commerce.model.ReorderSuggestion;
import com.example.commerce.model.enums.BusinessTrigger;
import org.springframework.stereotype.Service;

@Service
public class RuleReorderStrategy implements ReorderStrategy {

    @Override
    public ReorderSuggestion generateReorderRecommendation(Product product, BusinessTrigger trigger) {
        int recommendedQuantity = 0;
        String reasoning = "Default rule-based reorder recommendation";
        double confidence = 0.7;

        switch (trigger) {
            case INVENTORY_LOW:
                // Calculate reorder quantity based on demand velocity and desired safety stock
                int safetyStock = Math.max(product.getReorderThreshold() * 2, 20); // At least 20 units or 2x threshold
                recommendedQuantity = safetyStock - product.getStock() + (product.getDemandVelocity() * 2); // 2 weeks of demand
                reasoning = "Low inventory trigger - calculated reorder quantity based on demand velocity and safety stock";
                confidence = 0.8;
                break;
                
            case DEMAND_SPIKE:
                // For demand spike, increase reorder quantity to meet increased demand
                recommendedQuantity = product.getDemandVelocity() * 3; // 3 weeks of current demand
                reasoning = "Demand spike detected - increased reorder quantity to meet expected higher demand";
                confidence = 0.85;
                break;
        }

        // Ensure minimum reorder quantity
        recommendedQuantity = Math.max(recommendedQuantity, 10);

        return new ReorderSuggestion(product, trigger, recommendedQuantity, confidence, reasoning);
    }
}