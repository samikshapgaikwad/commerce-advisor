package com.example.commerce.repository;

import com.example.commerce.model.InventorySnapshot;
import com.example.commerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventorySnapshotRepository extends JpaRepository<InventorySnapshot, Long> {
    List<InventorySnapshot> findByProductOrderByTimestampDesc(Product product);
}