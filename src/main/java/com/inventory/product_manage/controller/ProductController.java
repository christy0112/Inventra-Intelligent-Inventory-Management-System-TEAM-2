package com.inventory.product_manage.controller;

import org.springframework.http.ResponseEntity;

import com.inventory.product_manage.model.Product;
import com.inventory.product_manage.model.Inventory;
import com.inventory.product_manage.repository.ProductRepository;
import com.inventory.product_manage.repository.InventoryRepository;
import com.inventory.product_manage.service.InventoryService;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryService inventoryService;   // 🔔 ADDED

    // ✅ CONSTRUCTOR INJECTION
    public ProductController(ProductRepository productRepository,
                             InventoryRepository inventoryRepository,
                             InventoryService inventoryService) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.inventoryService = inventoryService;
    }

    /* ================= DELETE PRODUCT ================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            // Delete inventory row first (if exists) to avoid FK constraint issues
            productRepository.findById(id).ifPresent(product ->
                    inventoryRepository.findByProduct(product)
                            .ifPresent(inventoryRepository::delete)
            );

            productRepository.deleteById(id);
            return ResponseEntity.ok("Product deleted successfully");
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Cannot delete product. Inventory exists for this product.");
        }
    }

    /* ================= PRODUCT LIST ================= */
    @GetMapping
    public List<Map<String, Object>> getAllProducts() {

        List<Product> products = productRepository.findAll();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Product p : products) {

            Map<String, Object> map = new HashMap<>();

            map.put("productId", p.getProductId());
            map.put("sku", p.getSku());
            map.put("name", p.getName());
            map.put("price", p.getPrice());

            map.put("categoryName",
                    p.getCategory() != null ? p.getCategory().getCategoryName() : null);
            map.put("supplierName",
                    p.getSupplier() != null ? p.getSupplier().getSupplierName() : null);

            map.put("quantity", p.getQuantity() == null ? 0 : p.getQuantity());

            response.add(map);
        }

        return response;
    }


    /* ================= UPDATE MIN STOCK (ADMIN) ================= */
    @PutMapping("/min-stock/{productId}/{minStock}")
    public ResponseEntity<String> updateMinStock(
            @PathVariable Long productId,
            @PathVariable int minStock) {

        String msg = inventoryService.updateMinStock(productId, minStock);
        return ResponseEntity.ok(msg);
    }
}
