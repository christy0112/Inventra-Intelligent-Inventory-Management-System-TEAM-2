package com.inventory.product_manage.model;

/**
 * DTO for product creation and update requests
 */
public class ProductRequest {
    
    private String sku;
    private String name;
    private Integer price;
    private Long categoryId;
    private Long supplierId;
    private Integer minStockLevel;
    private Integer quantity;

    // Constructors
    public ProductRequest() {
    }

    public ProductRequest(String sku, String name, Integer price, Long categoryId, 
                         Long supplierId, Integer minStockLevel) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
        this.supplierId = supplierId;
        this.minStockLevel = minStockLevel;
    }

    // Getters and Setters
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getMinStockLevel() {
        return minStockLevel;
    }

    public void setMinStockLevel(Integer minStockLevel) {
        this.minStockLevel = minStockLevel;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
