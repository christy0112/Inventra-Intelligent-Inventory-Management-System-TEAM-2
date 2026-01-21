package com.inventory.product_manage.service;

import com.inventory.alerts.service.AlertService;
import com.inventory.product_manage.model.Product;
import com.inventory.product_manage.repository.ProductRepository;
import com.inventory.transactions.model.TransactionType;
import com.inventory.transactions.service.TransactionService;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final ProductRepository productRepository;
    private final AlertService alertService;
    private final TransactionService transactionService;

    public InventoryService(ProductRepository productRepository,
                            AlertService alertService,
                            TransactionService transactionService) {
        this.productRepository = productRepository;
        this.alertService = alertService;
        this.transactionService = transactionService;
    }

    /* ---------------- STOCK IN ---------------- */
    public String stockIn(Long productId, int qty, Integer performedByUserId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int currentQty = product.getQuantity() == null ? 0 : product.getQuantity();
        int newQty = currentQty + qty;
        product.setQuantity(newQty);
        productRepository.save(product);

        transactionService.log(TransactionType.STOCK_IN, productId, qty, performedByUserId);

        // 🔔 CHECK ALERT AFTER UPDATE
        alertService.checkLowStock(
                product.getProductId(),
                newQty,
                product.getMinStockLevel() == null ? 0 : product.getMinStockLevel(),
                product.getName()
        );

        return "Stock added successfully";
    }

    /* ---------------- STOCK OUT ---------------- */
    public String stockOut(Long productId, int qty, Integer performedByUserId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int currentQty = product.getQuantity() == null ? 0 : product.getQuantity();

        if (currentQty < qty) {
            return "Insufficient stock";
        }

        int newQty = currentQty - qty;
        product.setQuantity(newQty);
        productRepository.save(product);

        transactionService.log(TransactionType.STOCK_OUT, productId, qty, performedByUserId);

        // 🔔 CHECK ALERT AFTER UPDATE
        alertService.checkLowStock(
                product.getProductId(),
                newQty,
                product.getMinStockLevel() == null ? 0 : product.getMinStockLevel(),
                product.getName()
        );

        return "Stock removed successfully";
    }

    /* ---------------- UPDATE THRESHOLD (ADMIN) ---------------- */
    public String updateMinStock(Long productId, int minStock) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setMinStockLevel(minStock);
        productRepository.save(product);

        return "Minimum stock level updated";
    }
}

