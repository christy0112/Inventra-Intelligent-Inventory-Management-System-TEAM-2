package com.inventory.reports.controller;

import com.inventory.reports.dto.ReportResponse;
import com.inventory.reports.service.ExportService;
import com.inventory.reports.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Report Controller - Module 5: Reports & Analytics
 * Handles report generation and export functionality
 */
@RestController
@RequestMapping("/api/reports")
@CrossOrigin("*")
public class ReportController {

    private final ReportService reportService;
    private final ExportService exportService;

    public ReportController(ReportService reportService, ExportService exportService) {
        this.reportService = reportService;
        this.exportService = exportService;
    }

    /**
     * Generate inventory report
     * Matches pseudocode: generateReport(filters)
     */
    @GetMapping("/generate")
    public ReportResponse generateReport() {
        return reportService.generate();
    }

    /**
     * Export report to CSV format
     * Matches pseudocode: export(format)
     */
    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportCsv() {
        byte[] csvData = exportService.exportToCsv();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "inventory_report.csv");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(csvData);
    }

    /**
     * Export report to PDF format
     * Matches pseudocode: export(format)
     */
    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportPdf() {
        byte[] pdfData = exportService.exportToPdf();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "inventory_report.pdf");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfData);
    }

    /**
     * Get report summary/analytics
     */
    @GetMapping("/summary")
    public String getReportSummary() {
        ReportResponse report = reportService.generate();
        return "Total Products: " + report.getRows().size() + 
               " | Generated at: " + report.getGeneratedAt();
    }
}
