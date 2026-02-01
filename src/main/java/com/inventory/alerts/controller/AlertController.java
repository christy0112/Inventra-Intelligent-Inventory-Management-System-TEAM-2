package com.inventory.alerts.controller;

import com.inventory.alerts.model.Alert;
import com.inventory.alerts.model.NotifySupplierRequest;
import com.inventory.alerts.repository.AlertRepository;
import com.inventory.auth.service.EmailService;
import com.inventory.product_manage.model.Product;
import com.inventory.product_manage.model.Supplier;
import com.inventory.product_manage.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertRepository alertRepository;
    private final ProductRepository productRepository;
    private final EmailService emailService;

    public AlertController(AlertRepository alertRepository,
                           ProductRepository productRepository,
                           EmailService emailService) {
        this.alertRepository = alertRepository;
        this.productRepository = productRepository;
        this.emailService = emailService;
    }

    @GetMapping("/open")
    public List<Alert> openAlerts() {
        return alertRepository.findByAlertStatus("OPEN");
    }

    @GetMapping("/all")
    public List<Alert> allAlerts() {
        return alertRepository.findAll();
    }

    @PostMapping("/notify-supplier")
    public ResponseEntity<String> notifySupplier(@RequestBody NotifySupplierRequest request) {
        if (request.getProductId() == null || request.getQuantity() == null || request.getQuantity() < 1) {
            return ResponseEntity.badRequest().body("Product ID and quantity (at least 1) are required.");
        }
        Product product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().body("Product not found.");
        }
        Supplier supplier = product.getSupplier();
        if (supplier == null || supplier.getEmail() == null || supplier.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body("Product has no supplier with email. Cannot send alert.");
        }
        String categoryName = product.getCategory() != null ? product.getCategory().getCategoryName() : null;
        emailService.sendLowStockAlertToSupplier(
                supplier.getEmail(),
                supplier.getSupplierName(),
                product.getName(),
                categoryName,
                request.getQuantity()
        );
        return ResponseEntity.ok("Email sent to supplier successfully.");
    }
}
