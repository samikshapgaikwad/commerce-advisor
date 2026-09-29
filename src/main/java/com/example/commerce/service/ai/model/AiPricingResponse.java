package com.example.commerce.service.ai.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AiPricingResponse {
    @JsonProperty("recommendedPrice")
    private double recommendedPrice;
    
    @JsonProperty("direction")
    private String direction;
    
    @JsonProperty("confidence")
    private double confidence;
    
    @JsonProperty("reasoning")
    private String reasoning;

    // Constructors
    public AiPricingResponse() {}

    // Getters and Setters
    public double getRecommendedPrice() {
        return recommendedPrice;
    }

    public void setRecommendedPrice(double recommendedPrice) {
        this.recommendedPrice = recommendedPrice;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
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