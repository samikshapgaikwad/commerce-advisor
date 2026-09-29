package com.example.commerce.model.dto;

import com.example.commerce.model.enums.BusinessTrigger;
import com.example.commerce.model.enums.PriceDirection;
import com.example.commerce.model.enums.SuggestionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PricingSuggestionDTO {
    private Long id;
    private Long productId;
    private String productName;
    private BusinessTrigger trigger;
    private BigDecimal recommendedPrice;
    private PriceDirection direction;
    private Double confidence;
    private String reasoning;
    private SuggestionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public PricingSuggestionDTO() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BusinessTrigger getTrigger() {
        return trigger;
    }

    public void setTrigger(BusinessTrigger trigger) {
        this.trigger = trigger;
    }

    public BigDecimal getRecommendedPrice() {
        return recommendedPrice;
    }

    public void setRecommendedPrice(BigDecimal recommendedPrice) {
        this.recommendedPrice = recommendedPrice;
    }

    public PriceDirection getDirection() {
        return direction;
    }

    public void setDirection(PriceDirection direction) {
        this.direction = direction;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public String getReasoning() {
        return reasoning;
    }

    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }

    public SuggestionStatus getStatus() {
        return status;
    }

    public void setStatus(SuggestionStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}