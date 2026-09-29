package com.example.commerce.controller;

import com.example.commerce.model.PricingSuggestion;
import com.example.commerce.model.ReorderSuggestion;
import com.example.commerce.model.dto.PricingSuggestionDTO;
import com.example.commerce.model.dto.ReorderSuggestionDTO;
import com.example.commerce.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/suggestions")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:4200"})
public class SuggestionController {
    
    private final SuggestionService suggestionService;
    
    @Autowired
    public SuggestionController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }
    
    @GetMapping("/pricing")
    public List<PricingSuggestionDTO> getPendingPricingSuggestions() {
        return suggestionService.getPendingPricingSuggestions().stream()
            .map(this::convertToPricingDTO)
            .collect(Collectors.toList());
    }
    
    @GetMapping("/reorder")
    public List<ReorderSuggestionDTO> getPendingReorderSuggestions() {
        return suggestionService.getPendingReorderSuggestions().stream()
            .map(this::convertToReorderDTO)
            .collect(Collectors.toList());
    }
    
    @PostMapping("/pricing/{id}/accept")
    public ResponseEntity<PricingSuggestionDTO> acceptPricingSuggestion(@PathVariable Long id) {
        try {
            PricingSuggestion suggestion = suggestionService.acceptPricingSuggestion(id);
            return ResponseEntity.ok(convertToPricingDTO(suggestion));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/pricing/{id}/reject")
    public ResponseEntity<PricingSuggestionDTO> rejectPricingSuggestion(@PathVariable Long id) {
        try {
            PricingSuggestion suggestion = suggestionService.rejectPricingSuggestion(id);
            return ResponseEntity.ok(convertToPricingDTO(suggestion));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/reorder/{id}/accept")
    public ResponseEntity<ReorderSuggestionDTO> acceptReorderSuggestion(@PathVariable Long id) {
        try {
            ReorderSuggestion suggestion = suggestionService.acceptReorderSuggestion(id);
            return ResponseEntity.ok(convertToReorderDTO(suggestion));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/reorder/{id}/reject")
    public ResponseEntity<ReorderSuggestionDTO> rejectReorderSuggestion(@PathVariable Long id) {
        try {
            ReorderSuggestion suggestion = suggestionService.rejectReorderSuggestion(id);
            return ResponseEntity.ok(convertToReorderDTO(suggestion));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    private PricingSuggestionDTO convertToPricingDTO(PricingSuggestion suggestion) {
        PricingSuggestionDTO dto = new PricingSuggestionDTO();
        dto.setId(suggestion.getId());
        dto.setProductId(suggestion.getProduct().getId());
        dto.setProductName(suggestion.getProduct().getName());
        dto.setTrigger(suggestion.getTrigger());
        dto.setRecommendedPrice(suggestion.getRecommendedPrice());
        dto.setDirection(suggestion.getDirection());
        dto.setConfidence(suggestion.getConfidence());
        dto.setReasoning(suggestion.getReasoning());
        dto.setStatus(suggestion.getStatus());
        dto.setCreatedAt(suggestion.getCreatedAt());
        dto.setUpdatedAt(suggestion.getUpdatedAt());
        return dto;
    }
    
    private ReorderSuggestionDTO convertToReorderDTO(ReorderSuggestion suggestion) {
        ReorderSuggestionDTO dto = new ReorderSuggestionDTO();
        dto.setId(suggestion.getId());
        dto.setProductId(suggestion.getProduct().getId());
        dto.setProductName(suggestion.getProduct().getName());
        dto.setTrigger(suggestion.getTrigger());
        dto.setRecommendedQuantity(suggestion.getRecommendedQuantity());
        dto.setConfidence(suggestion.getConfidence());
        dto.setReasoning(suggestion.getReasoning());
        dto.setStatus(suggestion.getStatus());
        dto.setCreatedAt(suggestion.getCreatedAt());
        dto.setUpdatedAt(suggestion.getUpdatedAt());
        return dto;
    }
}