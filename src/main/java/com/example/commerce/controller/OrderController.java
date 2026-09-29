package com.example.commerce.controller;

import com.example.commerce.model.Product;
import com.example.commerce.model.dto.OrderRequestDTO;
import com.example.commerce.service.InventoryService;
import com.example.commerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:4200"})
public class OrderController {
    
    private final ProductService productService;
    private final InventoryService inventoryService;
    
    @Autowired
    public OrderController(ProductService productService, InventoryService inventoryService) {
        this.productService = productService;
        this.inventoryService = inventoryService;
    }
    
    @PostMapping("/simulate")
    public ResponseEntity<String> simulateOrder(@RequestBody OrderRequestDTO orderRequest) {
        // Validate the request
        if (orderRequest.getProductId() == null || orderRequest.getQuantity() == null || orderRequest.getQuantity() <= 0) {
            return ResponseEntity.badRequest().body("Invalid order request");
        }
        
        // Find the product
        return productService.getProductById(orderRequest.getProductId())
            .map(product -> {
                // Check if we have enough stock
                if (product.getStock() < orderRequest.getQuantity()) {
                    return ResponseEntity.badRequest().body("Insufficient stock");
                }
                
                // Update stock and trigger business signals if needed
                inventoryService.updateStock(product, orderRequest.getQuantity());
                
                return ResponseEntity.ok("Order processed successfully");
            })
            .orElse(ResponseEntity.notFound().build());
    }
}