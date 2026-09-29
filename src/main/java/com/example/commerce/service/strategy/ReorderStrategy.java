package com.example.commerce.service.strategy;

import com.example.commerce.model.Product;
import com.example.commerce.model.ReorderSuggestion;
import com.example.commerce.model.enums.BusinessTrigger;

public interface ReorderStrategy {
    ReorderSuggestion generateReorderRecommendation(Product product, BusinessTrigger trigger);
}