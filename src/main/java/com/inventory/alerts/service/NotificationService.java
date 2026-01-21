package com.inventory.alerts.service;

import com.inventory.alerts.model.Alert;
import com.inventory.alerts.repository.AlertRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final AlertRepository alertRepository;

    public NotificationService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public void sendAlert(Long productId, String message) {
        Alert alert = new Alert();
        alert.setProductId(productId);
        alert.setAlertMessage(message);
        alert.setAlertStatus("OPEN");

        alertRepository.save(alert);
    }
}
