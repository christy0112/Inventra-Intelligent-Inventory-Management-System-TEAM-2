package com.inventory.alerts.controller;

import com.inventory.alerts.model.Alert;
import com.inventory.alerts.repository.AlertRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    private final AlertRepository alertRepository;

    public AlertController(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @GetMapping("/open")
    public List<Alert> openAlerts() {
        return alertRepository.findByAlertStatus("OPEN");
    }

    @GetMapping("/all")
    public List<Alert> allAlerts() {
        return alertRepository.findAll();
    }
}
