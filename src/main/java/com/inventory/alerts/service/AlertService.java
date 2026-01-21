package com.inventory.alerts.service;

import org.springframework.stereotype.Service;

@Service
public class AlertService {

    private final NotificationService notificationService;

    public AlertService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void checkLowStock(Long productId,
                              int quantity,
                              int minStock,
                              String productName) {

        if (quantity <= minStock) {
            String message = "Low stock alert for product: " + productName +
                    " (Remaining: " + quantity + ")";
            notificationService.sendAlert(productId, message);
        }
    }
}
