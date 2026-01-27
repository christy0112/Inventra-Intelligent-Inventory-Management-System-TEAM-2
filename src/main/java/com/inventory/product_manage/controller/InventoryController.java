package com.inventory.product_manage.controller;

import com.inventory.auth.service.JWTUtility;
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
    private final JWTUtility jwtUtility;

    public InventoryController(InventoryService inventoryService,
                               ProductService productService,
                               JWTUtility jwtUtility) {
        this.inventoryService = inventoryService;
        this.productService = productService;
        this.jwtUtility = jwtUtility;
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
    public String stockIn(@RequestBody StockRequest request,
                          @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // Use user ID from token if not provided in request
        Integer userId = request.getPerformedByUserId();
        if (userId == null) {
            userId = extractUserIdFromToken(authHeader);
        }
        // Ensure we have a user ID - if not, return error
        if (userId == null) {
            return "Error: User authentication required. Please login again.";
        }
        return inventoryService.stockIn(
            request.getProductId(), 
            request.getQuantity(), 
            userId
        );
    }

    /**
     * Stock In - Alternative endpoint with path variables (backward compatibility)
     */
    @PutMapping("/stock-in/{productId}/{qty}")
    public String stockInPath(@PathVariable Long productId,
                          @PathVariable int qty,
                          @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Integer userId = extractUserIdFromToken(authHeader);
        if (userId == null) {
            return "Error: User authentication required. Please login again.";
        }
        return inventoryService.stockIn(productId, qty, userId);
    }

    /**
     * Stock Out - Decrease product quantity
     * Matches pseudocode: decreaseStock(productId, quantity)
     */
    @PostMapping("/stock-out")
    public String stockOut(@RequestBody StockRequest request,
                          @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // Use user ID from token if not provided in request
        Integer userId = request.getPerformedByUserId();
        if (userId == null) {
            userId = extractUserIdFromToken(authHeader);
        }
        // Ensure we have a user ID - if not, return error
        if (userId == null) {
            return "Error: User authentication required. Please login again.";
        }
        return inventoryService.stockOut(
            request.getProductId(), 
            request.getQuantity(), 
            userId
        );
    }

    /**
     * Stock Out - Alternative endpoint with path variables (backward compatibility)
     */
    @PutMapping("/stock-out/{productId}/{qty}")
    public String stockOutPath(@PathVariable Long productId,
                           @PathVariable int qty,
                           @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Integer userId = extractUserIdFromToken(authHeader);
        if (userId == null) {
            return "Error: User authentication required. Please login again.";
        }
        return inventoryService.stockOut(productId, qty, userId);
    }

    /**
     * Extract user ID from JWT token in Authorization header
     */
    private Integer extractUserIdFromToken(String authHeader) {
        if (authHeader == null || authHeader.isEmpty()) {
            return null;
        }
        return jwtUtility.getUserIdFromToken(authHeader);
    }
}
