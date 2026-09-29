package com.example.commerce.repository;

import com.example.commerce.model.Product;
import com.example.commerce.model.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);
    List<Product> findByStatus(ProductStatus status);
    List<Product> findByStockLessThanEqualAndStatusNot(Integer threshold, ProductStatus status);
}