package com.inventory.reports.service;

import com.inventory.product_manage.model.Product;
import com.inventory.product_manage.repository.ProductRepository;
import com.inventory.reports.dto.ProductReportRow;
import com.inventory.reports.dto.ReportResponse;
import com.inventory.transactions.model.TransactionType;
import com.inventory.transactions.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    private final ProductRepository productRepository;
    private final TransactionRepository transactionRepository;

    public ReportService(ProductRepository productRepository,
                         TransactionRepository transactionRepository) {
        this.productRepository = productRepository;
        this.transactionRepository = transactionRepository;
    }

    public ReportResponse generate() {
        return generate(null, null);
    }

    public ReportResponse generate(LocalDate fromDate, LocalDate toDate) {
        boolean filterByDate = fromDate != null || toDate != null;
        List<Product> products = productRepository.findAll();
        List<ProductReportRow> rows = new ArrayList<>();

        LocalDateTime from = null;
        LocalDateTime to = null;
        if (fromDate != null && toDate != null) {
            from = fromDate.atStartOfDay();
            to = toDate.atTime(LocalTime.MAX);
        } else if (fromDate != null) {
            from = fromDate.atStartOfDay();
            to = fromDate.atTime(LocalTime.MAX);
        } else if (toDate != null) {
            from = toDate.atStartOfDay();
            to = toDate.atTime(LocalTime.MAX);
        }

        for (Product product : products) {
            ProductReportRow row = new ProductReportRow();
            row.setProductId(product.getProductId());
            row.setSku(product.getSku());
            row.setName(product.getName());
            row.setCategory(product.getCategory() != null ? product.getCategory().getCategoryName() : null);
            row.setSupplier(product.getSupplier() != null ? product.getSupplier().getSupplierName() : null);
            row.setCurrentStock(product.getQuantity() == null ? 0 : product.getQuantity());
            row.setMinStockLevel(product.getMinStockLevel() == null ? 0 : product.getMinStockLevel());

            Integer totalIn;
            Integer totalOut;
            if (from != null && to != null) {
                totalIn = transactionRepository.sumQuantityByProductAndTypeAndDateRange(
                        product.getProductId(), TransactionType.STOCK_IN, from, to);
                totalOut = transactionRepository.sumQuantityByProductAndTypeAndDateRange(
                        product.getProductId(), TransactionType.STOCK_OUT, from, to);
            } else {
                totalIn = transactionRepository.sumQuantityByProductAndType(
                        product.getProductId(), TransactionType.STOCK_IN);
                totalOut = transactionRepository.sumQuantityByProductAndType(
                        product.getProductId(), TransactionType.STOCK_OUT);
            }

            row.setTotalStockIn(totalIn);
            row.setTotalStockOut(totalOut);

            // If user filtered by date, only include products that had transactions in that range
            if (filterByDate) {
                int inVal = totalIn != null ? totalIn : 0;
                int outVal = totalOut != null ? totalOut : 0;
                if (inVal == 0 && outVal == 0) {
                    continue;
                }
            }

            rows.add(row);
        }

        ReportResponse response = new ReportResponse();
        response.setGeneratedAt(LocalDateTime.now());
        response.setRows(rows);
        return response;
    }
}
