package com.example.commerce.service.ai.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AiReorderResponse {
    @JsonProperty("recommendedQuantity")
    private int recommendedQuantity;
    
    @JsonProperty("confidence")
    private double confidence;
    
    @JsonProperty("reasoning")
    private String reasoning;

    // Constructors
    public AiReorderResponse() {}

    // Getters and Setters
    public int getRecommendedQuantity() {
        return recommendedQuantity;
    }

    public void setRecommendedQuantity(int recommendedQuantity) {
        this.recommendedQuantity = recommendedQuantity;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public String getReasoning() {
        return reasoning;
    }

    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }
}