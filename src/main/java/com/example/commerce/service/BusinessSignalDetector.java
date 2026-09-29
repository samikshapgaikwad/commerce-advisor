package com.example.commerce.service;

import com.example.commerce.model.Product;
import com.example.commerce.model.enums.BusinessTrigger;
import org.springframework.stereotype.Service;

@Service
public class BusinessSignalDetector {
    
    public boolean isInventoryLow(Product product) {
        return product.getStock() <= product.getReorderThreshold();
    }
    
    public boolean isDemandSpike(Product product, int previousVelocity) {
        // Simple heuristic: 50% increase in demand velocity
        return product.getDemandVelocity() > previousVelocity * 1.5;
    }
    
    public BusinessTrigger detectTrigger(Product product, int previousVelocity) {
        if (isInventoryLow(product)) {
            return BusinessTrigger.INVENTORY_LOW;
        }
        
        if (isDemandSpike(product, previousVelocity)) {
            return BusinessTrigger.DEMAND_SPIKE;
        }
        
        return null;
    }
}