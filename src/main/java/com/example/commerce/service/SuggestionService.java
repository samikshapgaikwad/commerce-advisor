package com.example.commerce.service;

import com.example.commerce.model.Product;
import com.example.commerce.model.PricingSuggestion;
import com.example.commerce.model.ReorderSuggestion;
import com.example.commerce.model.enums.BusinessTrigger;
import com.example.commerce.model.enums.SuggestionStatus;
import com.example.commerce.repository.PricingSuggestionRepository;
import com.example.commerce.repository.ReorderSuggestionRepository;
import com.example.commerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SuggestionService {
    
    private final PricingSuggestionRepository pricingSuggestionRepository;
    private final ReorderSuggestionRepository reorderSuggestionRepository;
    private final CommerceEngineService commerceEngineService;
    private final ProductService productService;
    
    @Autowired
    public SuggestionService(PricingSuggestionRepository pricingSuggestionRepository,
                            ReorderSuggestionRepository reorderSuggestionRepository,
                            CommerceEngineService commerceEngineService,
                            ProductService productService) {
        this.pricingSuggestionRepository = pricingSuggestionRepository;
        this.reorderSuggestionRepository = reorderSuggestionRepository;
        this.commerceEngineService = commerceEngineService;
        this.productService = productService;
    }
    
    public List<PricingSuggestion> getPendingPricingSuggestions() {
        return pricingSuggestionRepository.findByStatusOrderByCreatedAtDesc(SuggestionStatus.PENDING);
    }
    
    public List<ReorderSuggestion> getPendingReorderSuggestions() {
        return reorderSuggestionRepository.findByStatusOrderByCreatedAtDesc(SuggestionStatus.PENDING);
    }
    
    public ReorderSuggestion createReorderSuggestion(Product product, BusinessTrigger trigger) {
        // Check if there's already a pending suggestion for this product and trigger
        Optional<ReorderSuggestion> existingSuggestion = 
            reorderSuggestionRepository.findFirstByProductAndTriggerAndStatusOrderByCreatedAtDesc(
                product, trigger, SuggestionStatus.PENDING);
        
        if (existingSuggestion.isPresent()) {
            return existingSuggestion.get();
        }
        
        ReorderSuggestion suggestion = commerceEngineService.generateReorderRecommendation(product, trigger);
        return reorderSuggestionRepository.save(suggestion);
    }
    
    public PricingSuggestion createPricingSuggestion(Product product, BusinessTrigger trigger) {
        // Check if there's already a pending suggestion for this product and trigger
        Optional<PricingSuggestion> existingSuggestion = 
            pricingSuggestionRepository.findFirstByProductAndTriggerAndStatusOrderByCreatedAtDesc(
                product, trigger, SuggestionStatus.PENDING);
        
        if (existingSuggestion.isPresent()) {
            return existingSuggestion.get();
        }
        
        PricingSuggestion suggestion = commerceEngineService.generatePricingRecommendation(product, trigger);
        return pricingSuggestionRepository.save(suggestion);
    }
    
    @Transactional
    public PricingSuggestion acceptPricingSuggestion(Long suggestionId) {
        Optional<PricingSuggestion> optionalSuggestion = pricingSuggestionRepository.findById(suggestionId);
        if (!optionalSuggestion.isPresent()) {
            throw new RuntimeException("Pricing suggestion not found");
        }
        
        PricingSuggestion suggestion = optionalSuggestion.get();
        if (suggestion.getStatus() != SuggestionStatus.PENDING) {
            throw new RuntimeException("Pricing suggestion is not in PENDING status");
        }
        
        // Update product price
        Product product = suggestion.getProduct();
        product.setCurrentPrice(suggestion.getRecommendedPrice());
        productService.saveProduct(product);
        
        // Update suggestion status
        suggestion.setStatus(SuggestionStatus.ACCEPTED);
        
        return pricingSuggestionRepository.save(suggestion);
    }
    
    @Transactional
    public ReorderSuggestion acceptReorderSuggestion(Long suggestionId) {
        Optional<ReorderSuggestion> optionalSuggestion = reorderSuggestionRepository.findById(suggestionId);
        if (!optionalSuggestion.isPresent()) {
            throw new RuntimeException("Reorder suggestion not found");
        }
        
        ReorderSuggestion suggestion = optionalSuggestion.get();
        if (suggestion.getStatus() != SuggestionStatus.PENDING) {
            throw new RuntimeException("Reorder suggestion is not in PENDING status");
        }
        
        // Update product stock
        Product product = suggestion.getProduct();
        int newStock = product.getStock() + suggestion.getRecommendedQuantity();
        product.setStock(newStock);
        productService.saveProduct(product);
        
        // Update suggestion status
        suggestion.setStatus(SuggestionStatus.ACCEPTED);
        
        return reorderSuggestionRepository.save(suggestion);
    }
    
    @Transactional
    public PricingSuggestion rejectPricingSuggestion(Long suggestionId) {
        Optional<PricingSuggestion> optionalSuggestion = pricingSuggestionRepository.findById(suggestionId);
        if (!optionalSuggestion.isPresent()) {
            throw new RuntimeException("Pricing suggestion not found");
        }
        
        PricingSuggestion suggestion = optionalSuggestion.get();
        if (suggestion.getStatus() != SuggestionStatus.PENDING) {
            throw new RuntimeException("Pricing suggestion is not in PENDING status");
        }
        
        suggestion.setStatus(SuggestionStatus.REJECTED);
        return pricingSuggestionRepository.save(suggestion);
    }
    
    @Transactional
    public ReorderSuggestion rejectReorderSuggestion(Long suggestionId) {
        Optional<ReorderSuggestion> optionalSuggestion = reorderSuggestionRepository.findById(suggestionId);
        if (!optionalSuggestion.isPresent()) {
            throw new RuntimeException("Reorder suggestion not found");
        }
        
        ReorderSuggestion suggestion = optionalSuggestion.get();
        if (suggestion.getStatus() != SuggestionStatus.PENDING) {
            throw new RuntimeException("Reorder suggestion is not in PENDING status");
        }
    
        suggestion.setStatus(SuggestionStatus.REJECTED);
        return reorderSuggestionRepository.save(suggestion);
    }
}