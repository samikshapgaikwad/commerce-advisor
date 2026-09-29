package com.example.commerce.service.event;

import com.example.commerce.model.Product;
import com.example.commerce.model.enums.BusinessTrigger;
import org.springframework.context.ApplicationEvent;

public class BusinessEvent extends ApplicationEvent {
    private final Product product;
    private final BusinessTrigger trigger;
    private final int previousVelocity;
    
    public BusinessEvent(Object source, Product product, BusinessTrigger trigger, int previousVelocity) {
        super(source);
        this.product = product;
        this.trigger = trigger;
        this.previousVelocity = previousVelocity;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public BusinessTrigger getTrigger() {
        return trigger;
    }
    
    public int getPreviousVelocity() {
        return previousVelocity;
    }
}