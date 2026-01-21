package com.inventory.reports.service;

import com.inventory.reports.dto.ProductReportRow;
import com.inventory.reports.dto.ReportResponse;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.StringJoiner;

/**
 * Export Service - Module 5: Reports & Analytics
 * Handles exporting reports to different formats (CSV, PDF)
 * Matches pseudocode: ExportService.export(format)
 */
@Service
public class ExportService {

    private final ReportService reportService;

    public ExportService(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Export report to CSV format
     * Matches pseudocode: IF format == CSV THEN generate CSV
     */
    public byte[] exportToCsv() {
        ReportResponse report = reportService.generate();
        String csvContent = generateCsvContent(report.getRows());
        return csvContent.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Export report to PDF format
     * Matches pseudocode: ELSE IF format == PDF THEN generate PDF
     */
    public byte[] exportToPdf() {
        ReportResponse report = reportService.generate();
        String pdfContent = generatePdfContent(report.getRows());
        // For now, returning text-based PDF placeholder
        // In production, use iText or Apache PDFBox for real PDF generation
        return pdfContent.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Generate CSV content from report rows
     */
    private String generateCsvContent(List<ProductReportRow> rows) {
        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        joiner.add("ProductId,SKU,Name,Category,Supplier,CurrentStock,MinStock,TotalIn,TotalOut");

        for (ProductReportRow row : rows) {
            joiner.add(String.join(",",
                    String.valueOf(row.getProductId()),
                    safe(row.getSku()),
                    safe(row.getName()),
                    safe(row.getCategory()),
                    safe(row.getSupplier()),
                    String.valueOf(row.getCurrentStock()),
                    String.valueOf(row.getMinStockLevel()),
                    String.valueOf(row.getTotalStockIn() != null ? row.getTotalStockIn() : 0),
                    String.valueOf(row.getTotalStockOut() != null ? row.getTotalStockOut() : 0)
            ));
        }
        return joiner.toString();
    }

    /**
     * Generate PDF content (placeholder implementation)
     * In production, integrate iText or Apache PDFBox
     */
    private String generatePdfContent(List<ProductReportRow> rows) {
        StringBuilder pdf = new StringBuilder();
        pdf.append("INVENTORY REPORT\n");
        pdf.append("================\n\n");
        
        for (ProductReportRow row : rows) {
            pdf.append("Product ID: ").append(row.getProductId()).append("\n");
            pdf.append("SKU: ").append(safe(row.getSku())).append("\n");
            pdf.append("Name: ").append(safe(row.getName())).append("\n");
            pdf.append("Category: ").append(safe(row.getCategory())).append("\n");
            pdf.append("Supplier: ").append(safe(row.getSupplier())).append("\n");
            pdf.append("Current Stock: ").append(row.getCurrentStock()).append("\n");
            pdf.append("Min Stock Level: ").append(row.getMinStockLevel()).append("\n");
            pdf.append("Total Stock In: ").append(row.getTotalStockIn() != null ? row.getTotalStockIn() : 0).append("\n");
            pdf.append("Total Stock Out: ").append(row.getTotalStockOut() != null ? row.getTotalStockOut() : 0).append("\n");
            pdf.append("----------------------------------------\n\n");
        }
        
        return pdf.toString();
    }

    /**
     * Helper method to safely handle null values in CSV
     */
    private String safe(String value) {
        return value == null ? "" : value.replace(",", " ");
    }
}
