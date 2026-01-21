package com.inventory.product_manage.model;

/**
 * DTO for stock in/out operation requests
 */
public class StockRequest {
    
    private Long productId;
    private Integer quantity;
    private Integer performedByUserId;

    // Constructors
    public StockRequest() {
    }

    public StockRequest(Long productId, Integer quantity, Integer performedByUserId) {
        this.productId = productId;
        this.quantity = quantity;
        this.performedByUserId = performedByUserId;
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPerformedByUserId() {
        return performedByUserId;
    }

    public void setPerformedByUserId(Integer performedByUserId) {
        this.performedByUserId = performedByUserId;
    }
}
