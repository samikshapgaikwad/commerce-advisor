package com.example.commerce.service;

import com.example.commerce.model.InventorySnapshot;
import com.example.commerce.model.Product;
import com.example.commerce.model.enums.BusinessTrigger;
import com.example.commerce.repository.InventorySnapshotRepository;
import com.example.commerce.service.event.BusinessEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {
    
    private final InventorySnapshotRepository inventorySnapshotRepository;
    private final BusinessEventPublisher businessEventPublisher;
    private final BusinessSignalDetector businessSignalDetector;
    
    @Autowired
    public InventoryService(InventorySnapshotRepository inventorySnapshotRepository,
                           BusinessEventPublisher businessEventPublisher,
                           BusinessSignalDetector businessSignalDetector) {
        this.inventorySnapshotRepository = inventorySnapshotRepository;
        this.businessEventPublisher = businessEventPublisher;
        this.businessSignalDetector = businessSignalDetector;
    }
    
    @Transactional
    public void updateStock(Product product, int quantityChange) {
        int previousVelocity = product.getDemandVelocity();
        
        // Update stock
        int newStock = product.getStock() - quantityChange;
        product.setStock(newStock);
        
        // Create inventory snapshot
        InventorySnapshot snapshot = new InventorySnapshot(product, newStock);
        inventorySnapshotRepository.save(snapshot);
        
        // Check for business signals
        BusinessTrigger trigger = businessSignalDetector.detectTrigger(product, previousVelocity);
        if (trigger != null) {
            businessEventPublisher.publishBusinessEvent(product, trigger, previousVelocity);
        }
    }
}