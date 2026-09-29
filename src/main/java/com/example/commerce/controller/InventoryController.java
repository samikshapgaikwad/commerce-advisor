package com.example.commerce.controller;

import com.example.commerce.model.InventorySnapshot;
import com.example.commerce.model.Product;
import com.example.commerce.repository.InventorySnapshotRepository;
import com.example.commerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:4200"})
public class InventoryController {
    
    private final ProductService productService;
    private final InventorySnapshotRepository inventorySnapshotRepository;
    
    @Autowired
    public InventoryController(ProductService productService, 
                              InventorySnapshotRepository inventorySnapshotRepository) {
        this.productService = productService;
        this.inventorySnapshotRepository = inventorySnapshotRepository;
    }
    
    @GetMapping("/{productId}")
    public ResponseEntity<List<InventorySnapshot>> getInventoryHistory(@PathVariable Long productId) {
        Optional<Product> product = productService.getProductById(productId);
        if (product.isPresent()) {
            List<InventorySnapshot> snapshots = inventorySnapshotRepository.findByProductOrderByTimestampDesc(product.get());
            return ResponseEntity.ok(snapshots);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}