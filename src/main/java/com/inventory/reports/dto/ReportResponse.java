package com.inventory.reports.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ReportResponse {

    private LocalDateTime generatedAt;
    private List<ProductReportRow> rows;

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public List<ProductReportRow> getRows() {
        return rows;
    }

    public void setRows(List<ProductReportRow> rows) {
        this.rows = rows;
    }
}
