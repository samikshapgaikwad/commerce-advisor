package com.example.commerce.service.strategy;

import com.example.commerce.model.Product;
import com.example.commerce.model.PricingSuggestion;
import com.example.commerce.model.enums.BusinessTrigger;

public interface PricingStrategy {
    PricingSuggestion generatePricingRecommendation(Product product, BusinessTrigger trigger);
}