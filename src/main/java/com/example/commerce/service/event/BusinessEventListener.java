package com.example.commerce.service.event;

import com.example.commerce.model.enums.BusinessTrigger;
import com.example.commerce.service.BusinessSignalDetector;
import com.example.commerce.service.SuggestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class BusinessEventListener {
    
    private static final Logger logger = LoggerFactory.getLogger(BusinessEventListener.class);
    
    private final SuggestionService suggestionService;
    
    @Autowired
    public BusinessEventListener(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }
    
    @Async
    @EventListener
    public void handleBusinessEvent(BusinessEvent event) {
        try {
            logger.info("Processing business event for product ID: {} with trigger: {}", 
                       event.getProduct().getId(), event.getTrigger());
            
            // Generate appropriate suggestions based on the trigger
            if (event.getTrigger() == BusinessTrigger.INVENTORY_LOW) {
                logger.info("Generating reorder suggestion for low inventory product: {}", 
                           event.getProduct().getName());
                suggestionService.createReorderSuggestion(event.getProduct(), event.getTrigger());
            } else if (event.getTrigger() == BusinessTrigger.DEMAND_SPIKE) {
                logger.info("Generating pricing and reorder suggestions for demand spike product: {}", 
                           event.getProduct().getName());
                suggestionService.createPricingSuggestion(event.getProduct(), event.getTrigger());
                suggestionService.createReorderSuggestion(event.getProduct(), event.getTrigger());
            }
        } catch (Exception e) {
            logger.error("Failed to process business event for product: " + event.getProduct().getId(), e);
        }
    }
}