package com.example.commerce.model;

import com.example.commerce.model.enums.ProductStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String sku;

    @NotBlank
    private String name;

    private String category;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal currentPrice;

    @Min(0)
    private Integer stock = 0;

    @Min(0)
    private Integer reorderThreshold = 0;

    @Min(0)
    private Integer demandVelocity = 0;

    @Enumerated(EnumType.STRING)
    private ProductStatus status = ProductStatus.ACTIVE;

    // Future extensible fields
    private BigDecimal costPrice;
    private BigDecimal marginFloor;
    private String supplierId;

    // Constructors
    public Product() {}

    public Product(String sku, String name, String category, BigDecimal currentPrice, 
                   Integer stock, Integer reorderThreshold, Integer demandVelocity) {
        this.sku = sku;
        this.name = name;
        this.category = category;
        this.currentPrice = currentPrice;
        this.stock = stock;
        this.reorderThreshold = reorderThreshold;
        this.demandVelocity = demandVelocity;
        this.status = determineStatus();
    }

    // Methods
    public boolean isLowStock() {
        return this.stock <= this.reorderThreshold;
    }

    public ProductStatus determineStatus() {
        if (isLowStock()) {
            return ProductStatus.LOW_STOCK;
        }
        return ProductStatus.ACTIVE;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
        this.status = determineStatus();
    }

    public Integer getReorderThreshold() {
        return reorderThreshold;
    }

    public void setReorderThreshold(Integer reorderThreshold) {
        this.reorderThreshold = reorderThreshold;
        this.status = determineStatus();
    }

    public Integer getDemandVelocity() {
        return demandVelocity;
    }

    public void setDemandVelocity(Integer demandVelocity) {
        this.demandVelocity = demandVelocity;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getMarginFloor() {
        return marginFloor;
    }

    public void setMarginFloor(BigDecimal marginFloor) {
        this.marginFloor = marginFloor;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
}