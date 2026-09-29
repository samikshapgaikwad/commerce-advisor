package com.example.commerce.service.event;

import com.example.commerce.model.Product;
import com.example.commerce.model.enums.BusinessTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class BusinessEventPublisher {
    
    private final ApplicationEventPublisher eventPublisher;
    
    @Autowired
    public BusinessEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
    
    public void publishBusinessEvent(Product product, BusinessTrigger trigger, int previousVelocity) {
        BusinessEvent event = new BusinessEvent(this, product, trigger, previousVelocity);
        eventPublisher.publishEvent(event);
    }
}