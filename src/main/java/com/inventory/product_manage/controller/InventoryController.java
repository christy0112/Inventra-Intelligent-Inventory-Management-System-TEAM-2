package com.inventory.product_manage.controller;

import com.inventory.product_manage.service.InventoryService;
import com.inventory.product_manage.service.ProductService;
import com.inventory.product_manage.model.Product;
import com.inventory.product_manage.model.ProductRequest;
import com.inventory.product_manage.model.StockRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin("*")
public class InventoryController {

    private final InventoryService inventoryService;
    private final ProductService productService;

    public InventoryController(InventoryService inventoryService,
                               ProductService productService) {
        this.inventoryService = inventoryService;
        this.productService = productService;
    }

    /**
     * Add a new product to inventory
     */
    @PostMapping("/product")
    public String addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    /**
     * Stock In - Increase product quantity
     * Matches pseudocode: increaseStock(productId, quantity)
     */
    @PostMapping("/stock-in")
    public String stockIn(@RequestBody StockRequest request) {
        return inventoryService.stockIn(
            request.getProductId(), 
            request.getQuantity(), 
            request.getPerformedByUserId()
        );
    }

    /**
     * Stock In - Alternative endpoint with path variables (backward compatibility)
     */
    @PutMapping("/stock-in/{productId}/{qty}")
    public String stockInPath(@PathVariable Long productId,
                          @PathVariable int qty,
                          @RequestHeader(value = "X-User-Id", required = false) Integer userId) {
        return inventoryService.stockIn(productId, qty, userId);
    }

    /**
     * Stock Out - Decrease product quantity
     * Matches pseudocode: decreaseStock(productId, quantity)
     */
    @PostMapping("/stock-out")
    public String stockOut(@RequestBody StockRequest request) {
        return inventoryService.stockOut(
            request.getProductId(), 
            request.getQuantity(), 
            request.getPerformedByUserId()
        );
    }

    /**
     * Stock Out - Alternative endpoint with path variables (backward compatibility)
     */
    @PutMapping("/stock-out/{productId}/{qty}")
    public String stockOutPath(@PathVariable Long productId,
                           @PathVariable int qty,
                           @RequestHeader(value = "X-User-Id", required = false) Integer userId) {
        return inventoryService.stockOut(productId, qty, userId);
    }
}
