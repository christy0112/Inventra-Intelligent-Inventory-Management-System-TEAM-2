package com.inventory.reports.dto;

public class ProductReportRow {
    private Long productId;
    private String sku;
    private String name;
    private String category;
    private String supplier;
    private Integer currentStock;
    private Integer minStockLevel;
    private Integer totalStockIn;
    private Integer totalStockOut;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

    public Integer getMinStockLevel() {
        return minStockLevel;
    }

    public void setMinStockLevel(Integer minStockLevel) {
        this.minStockLevel = minStockLevel;
    }

    public Integer getTotalStockIn() {
        return totalStockIn;
    }

    public void setTotalStockIn(Integer totalStockIn) {
        this.totalStockIn = totalStockIn;
    }

    public Integer getTotalStockOut() {
        return totalStockOut;
    }

    public void setTotalStockOut(Integer totalStockOut) {
        this.totalStockOut = totalStockOut;
    }
}
