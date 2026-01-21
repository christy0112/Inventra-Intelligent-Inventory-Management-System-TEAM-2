package com.inventory.product_manage.model;

/**
 * DTO for product response data
 */
public class ProductResponse {
    
    private Long productId;
    private String sku;
    private String name;
    private Integer price;
    private String categoryName;
    private String supplierName;
    private Integer quantity;
    private Integer minStockLevel;

    // Constructors
    public ProductResponse() {
    }

    public ProductResponse(Long productId, String sku, String name, Integer price,
                          String categoryName, String supplierName, Integer quantity, 
                          Integer minStockLevel) {
        this.productId = productId;
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.categoryName = categoryName;
        this.supplierName = supplierName;
        this.quantity = quantity;
        this.minStockLevel = minStockLevel;
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getMinStockLevel() {
        return minStockLevel;
    }

    public void setMinStockLevel(Integer minStockLevel) {
        this.minStockLevel = minStockLevel;
    }
}
