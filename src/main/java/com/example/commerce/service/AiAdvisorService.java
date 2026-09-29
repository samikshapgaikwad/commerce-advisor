package com.example.commerce.service;

import com.example.commerce.service.ai.model.AiPricingResponse;
import com.example.commerce.service.ai.model.AiReorderResponse;
import com.example.commerce.model.Product;
import com.example.commerce.model.enums.BusinessTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.HashMap;
import java.util.Map;

@Service
public class AiAdvisorService {
    
    private static final Logger logger = LoggerFactory.getLogger(AiAdvisorService.class);
    
    @Value("${llm.api.url:https://litellm-qc.zycus.net/v1/chat/completions}")
    private String apiUrl;
    
    @Value("${llm.api.key:sk-SfyNGxhcv7RnQKbZFWX2LQ}")
    private String apiKey;
    
    @Value("${llm.api.model:qwen-cursor}")
    private String model;
    
    private final RestTemplate restTemplate;
    
    public AiAdvisorService() {
        this.restTemplate = new RestTemplate();
    }
    
    public AiPricingResponse generatePricingRecommendation(Product product, BusinessTrigger trigger) {
        try {
            String prompt = generatePricingPrompt(product, trigger);
            String response = callLLM(prompt);
            
            // Parse the JSON response from the LLM
            return parsePricingResponse(response);
        } catch (Exception e) {
            logger.error("Failed to get AI pricing recommendation: " + e.getMessage(), e);
            throw new RuntimeException("AI service unavailable", e);
        }
    }
    
    public AiReorderResponse generateReorderRecommendation(Product product, BusinessTrigger trigger) {
        try {
            String prompt = generateReorderPrompt(product, trigger);
            String response = callLLM(prompt);
            
            // Parse the JSON response from the LLM
            return parseReorderResponse(response);
        } catch (Exception e) {
            logger.error("Failed to get AI reorder recommendation: " + e.getMessage(), e);
            throw new RuntimeException("AI service unavailable", e);
        }
    }
    
    private String generatePricingPrompt(Product product, BusinessTrigger trigger) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("As an AI commerce advisor, analyze this product and provide pricing recommendations.\n\n");
        prompt.append("Product Details:\n");
        prompt.append("- SKU: ").append(product.getSku()).append("\n");
        prompt.append("- Name: ").append(product.getName()).append("\n");
        prompt.append("- Category: ").append(product.getCategory()).append("\n");
        prompt.append("- Current Price: $").append(product.getCurrentPrice()).append("\n");
        prompt.append("- Current Stock: ").append(product.getStock()).append("\n");
        prompt.append("- Reorder Threshold: ").append(product.getReorderThreshold()).append("\n");
        prompt.append("- Demand Velocity: ").append(product.getDemandVelocity()).append("\n");
        prompt.append("- Trigger: ").append(trigger).append("\n\n");
        
        if (trigger == BusinessTrigger.INVENTORY_LOW) {
            prompt.append("Context: The product is experiencing low inventory. Consider the tradeoff between:\n");
            prompt.append("1. Increasing price to reduce demand and preserve limited inventory\n");
            prompt.append("2. Maintaining price to keep sales flowing\n");
            prompt.append("3. Decreasing price to clear remaining inventory quickly\n\n");
        } else if (trigger == BusinessTrigger.DEMAND_SPIKE) {
            prompt.append("Context: The product is experiencing a sudden increase in demand. Consider:\n");
            prompt.append("- The impact of increased demand on pricing strategy\n");
            prompt.append("- Balancing revenue optimization with inventory availability\n");
            prompt.append("- Potential for temporary price adjustments\n\n");
        }
        
        prompt.append("Provide a JSON response with the following structure:\n");
        prompt.append("{\n");
        prompt.append("  \"recommendedPrice\": [number],\n");
        prompt.append("  \"direction\": \"[INCREASE|DECREASE|MAINTAIN]\",\n");
        prompt.append("  \"confidence\": [number between 0 and 1],\n");
        prompt.append("  \"reasoning\": \"[explanation of the recommendation]\"\n");
        prompt.append("}\n\n");
        prompt.append("Be specific about the reasoning and quantify your confidence.");
        
        return prompt.toString();
    }
    
    private String generateReorderPrompt(Product product, BusinessTrigger trigger) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("As an AI commerce advisor, analyze this product and provide reorder recommendations.\n\n");
        prompt.append("Product Details:\n");
        prompt.append("- SKU: ").append(product.getSku()).append("\n");
        prompt.append("- Name: ").append(product.getName()).append("\n");
        prompt.append("- Category: ").append(product.getCategory()).append("\n");
        prompt.append("- Current Price: $").append(product.getCurrentPrice()).append("\n");
        prompt.append("- Current Stock: ").append(product.getStock()).append("\n");
        prompt.append("- Reorder Threshold: ").append(product.getReorderThreshold()).append("\n");
        prompt.append("- Demand Velocity: ").append(product.getDemandVelocity()).append("\n");
        prompt.append("- Trigger: ").append(trigger).append("\n\n");
        
        if (trigger == BusinessTrigger.INVENTORY_LOW) {
            prompt.append("Context: The product is experiencing low inventory. Consider:\n");
            prompt.append("1. The urgency of replenishment based on demand velocity\n");
            prompt.append("2. Optimal reorder quantity to balance carrying costs with stockout risks\n");
            prompt.append("3. Timing of the reorder to minimize disruption\n\n");
        } else if (trigger == BusinessTrigger.DEMAND_SPIKE) {
            prompt.append("Context: The product is experiencing a sudden increase in demand. Consider:\n");
            prompt.append("- The sustainability of the demand spike\n");
            prompt.append("- Whether current inventory will meet the increased demand\n");
            prompt.append("- Optimal reorder quantity to capture the opportunity\n\n");
        }
        
        prompt.append("Provide a JSON response with the following structure:\n");
        prompt.append("{\n");
        prompt.append("  \"recommendedQuantity\": [number],\n");
        prompt.append("  \"confidence\": [number between 0 and 1],\n");
        prompt.append("  \"reasoning\": \"[explanation of the recommendation]\"\n");
        prompt.append("}\n\n");
        prompt.append("Be specific about the reasoning and quantify your confidence.");
        
        return prompt.toString();
    }
    private String callLLM(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);
            headers.set("product", "PC1");
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            
            Map<String, Object> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);
            
            @SuppressWarnings("unchecked")
            Map<String, Object>[] messages = new Map[]{message};
            requestBody.put("messages", messages);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);
            return response.getBody();
        } catch (RestClientException e) {
            logger.error("Failed to call LLM API: " + e.getMessage(), e);
            throw new RuntimeException("Failed to communicate with AI service", e);
        }
    }
    
    private AiPricingResponse parsePricingResponse(String jsonResponse) {
        try {
            // Extract the content from the LLM response
            // The response is typically in the format: {"choices": [{"message": {"content": "..."}}]}
            // We need to extract the content and then parse it as JSON
            
            // For simplicity, we'll assume the LLM returns a valid JSON object directly
            // In a production environment, you would need more robust parsing
            
            ObjectMapper mapper = new ObjectMapper();
            
            // First, try to parse as a direct JSON object
            try {
                return mapper.readValue(jsonResponse, AiPricingResponse.class);
            } catch (JsonProcessingException e) {
                // If that fails, try to extract from the choices array
                JsonNode rootNode = mapper.readTree(jsonResponse);
                JsonNode choicesNode = rootNode.path("choices");
                
                if (choicesNode.isArray() && choicesNode.size() > 0) {
                    JsonNode messageNode = choicesNode.get(0).path("message");
                    JsonNode contentNode = messageNode.path("content");
                    
                    if (contentNode.isTextual()) {
                        String content = contentNode.asText();
                        return mapper.readValue(content, AiPricingResponse.class);
                    }
                }
                
                throw new RuntimeException("Unable to parse pricing response: " + jsonResponse);
            }
        } catch (Exception e) {
            logger.error("Failed to parse pricing response: " + jsonResponse, e);
            // Return a default response in case of parsing failure
            AiPricingResponse defaultResponse = new AiPricingResponse();
            defaultResponse.setRecommendedPrice(0.0);
            defaultResponse.setDirection("MAINTAIN");
            defaultResponse.setConfidence(0.0);
            defaultResponse.setReasoning("Failed to parse AI response");
            return defaultResponse;
        }
    }
    
    private AiReorderResponse parseReorderResponse(String jsonResponse) {
        try {
            // Extract the content from the LLM response
            // Similar logic as parsePricingResponse
            
            ObjectMapper mapper = new ObjectMapper();
            
            // First, try to parse as a direct JSON object
            try {
                return mapper.readValue(jsonResponse, AiReorderResponse.class);
            } catch (JsonProcessingException e) {
                // If that fails, try to extract from the choices array
                JsonNode rootNode = mapper.readTree(jsonResponse);
                JsonNode choicesNode = rootNode.path("choices");
                
                if (choicesNode.isArray() && choicesNode.size() > 0) {
                    JsonNode messageNode = choicesNode.get(0).path("message");
                    JsonNode contentNode = messageNode.path("content");
                    
                    if (contentNode.isTextual()) {
                        String content = contentNode.asText();
                        return mapper.readValue(content, AiReorderResponse.class);
                    }
                }
                
                throw new RuntimeException("Unable to parse reorder response: " + jsonResponse);
            }
        } catch (Exception e) {
            logger.error("Failed to parse reorder response: " + jsonResponse, e);
            // Return a default response in case of parsing failure
            AiReorderResponse defaultResponse = new AiReorderResponse();
            defaultResponse.setRecommendedQuantity(0);
            defaultResponse.setConfidence(0.0);
            defaultResponse.setReasoning("Failed to parse AI response");
            return defaultResponse;
        }
    }
}