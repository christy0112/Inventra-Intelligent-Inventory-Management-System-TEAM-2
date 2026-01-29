package com.inventory.reports.service;

import com.inventory.reports.dto.ProductReportRow;
import com.inventory.reports.dto.ReportResponse;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringJoiner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

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

    public byte[] exportToCsv(LocalDate fromDate, LocalDate toDate) {
        ReportResponse report = reportService.generate(fromDate, toDate);
        String csvContent = generateCsvContent(report.getRows());
        return csvContent.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Export report to PDF format
     * Matches pseudocode: ELSE IF format == PDF THEN generate PDF
     */
    public byte[] exportToPdf() {
        return exportToPdf(null, null);
    }

    public byte[] exportToPdf(LocalDate fromDate, LocalDate toDate) {
        ReportResponse report = reportService.generate(fromDate, toDate);
        return generatePdfBytes(report, fromDate, toDate);
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

    private byte[] generatePdfBytes(ReportResponse report, LocalDate fromDate, LocalDate toDate) {
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            // Define fonts compatible with PDFBox 3.x
            PDType1Font fontTitle = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDType1Font fontNormal = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            PDType1Font fontHeader = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);

            float margin = 40;
            float yStart = page.getMediaBox().getHeight() - margin;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float rowHeight = 18;

            float[] colWidths = new float[]{
                    40,   // ID
                    70,   // SKU
                    160,  // Name
                    70,   // Current
                    70,   // In
                    70    // Out
            };

            PDPageContentStream content = new PDPageContentStream(document, page);

            // Title
            content.beginText();
            content.setFont(fontTitle, 16);
            content.newLineAtOffset(margin, yStart);
            content.showText("Inventory Report");
            content.endText();

            float y = yStart - 24;

            // Generated at + range
            String range = buildRangeLabel(fromDate, toDate);
            content.beginText();
            content.setFont(fontNormal, 11);
            content.newLineAtOffset(margin, y);
            content.showText("Generated at: " + report.getGeneratedAt() + (range.isEmpty() ? "" : " | " + range));
            content.endText();

            y -= 24;

            // Draw table header row
            drawTableRow(content, fontHeader, margin, y, rowHeight, colWidths,
                    new String[]{"ID", "SKU", "Name", "Current", "TotalIn", "TotalOut"});

            y -= rowHeight;

            // Draw data rows
            for (ProductReportRow row : report.getRows()) {
                if (y < margin + rowHeight * 2) {
                    // finish current page and start a new one
                    content.close();
                    page = new PDPage(PDRectangle.LETTER);
                    document.addPage(page);
                    y = page.getMediaBox().getHeight() - margin - 40;
                    content = new PDPageContentStream(document, page);

                    // redraw header on new page
                    drawTableRow(content, fontHeader, margin, y, rowHeight, colWidths,
                            new String[]{"ID", "SKU", "Name", "Current", "TotalIn", "TotalOut"});
                    y -= rowHeight;
                }

                String[] values = new String[]{
                        String.valueOf(row.getProductId()),
                        safe(row.getSku()),
                        safe(row.getName()),
                        String.valueOf(row.getCurrentStock() != null ? row.getCurrentStock() : 0),
                        String.valueOf(row.getTotalStockIn() != null ? row.getTotalStockIn() : 0),
                        String.valueOf(row.getTotalStockOut() != null ? row.getTotalStockOut() : 0)
                };

                drawTableRow(content, fontNormal, margin, y, rowHeight, colWidths, values);
                y -= rowHeight;
            }

            content.close();

            document.save(out);
            return out.toByteArray();
        } catch (Exception e) {
            // fallback: never break export completely
            String fallback = "Inventory report could not be generated as PDF. Error: " + e.getMessage();
            return fallback.getBytes(StandardCharsets.UTF_8);
        }
    }

    private String formatRow(ProductReportRow row) {
        String name = safe(row.getName());
        if (name.length() > 22) name = name.substring(0, 22) + "...";
        return String.format("%d  %s  %s  %d  %d  %d",
                row.getProductId(),
                safe(row.getSku()),
                name,
                row.getCurrentStock() != null ? row.getCurrentStock() : 0,
                row.getTotalStockIn() != null ? row.getTotalStockIn() : 0,
                row.getTotalStockOut() != null ? row.getTotalStockOut() : 0
        );
    }

    /**
     * Draw a single table row (borders + cell text) at the given Y position.
     */
    private void drawTableRow(PDPageContentStream content,
                              PDType1Font font,
                              float startX,
                              float y,
                              float rowHeight,
                              float[] colWidths,
                              String[] values) throws java.io.IOException {

        float x = startX;
        float textOffsetY = y - (rowHeight / 2) + 3;

        // Draw cell borders and text
        for (int i = 0; i < colWidths.length; i++) {
            float cellWidth = colWidths[i];

            // Cell border
            content.addRect(x, y - rowHeight, cellWidth, rowHeight);

            // Cell text
            content.beginText();
            content.setFont(font, 9);
            content.newLineAtOffset(x + 2, textOffsetY);
            String text = i < values.length ? values[i] : "";
            content.showText(text);
            content.endText();

            x += cellWidth;
        }

        content.stroke();
    }

    private String buildRangeLabel(LocalDate fromDate, LocalDate toDate) {
        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE;
        if (fromDate == null && toDate == null) return "";
        if (fromDate != null && toDate != null) return "Date range: " + fmt.format(fromDate) + " to " + fmt.format(toDate);
        if (fromDate != null) return "Date: " + fmt.format(fromDate);
        return "Date: " + fmt.format(toDate);
    }

    /**
     * Helper method to safely handle null values in CSV
     */
    private String safe(String value) {
        return value == null ? "" : value.replace(",", " ");
    }
}
