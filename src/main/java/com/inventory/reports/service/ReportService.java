package com.inventory.reports.service;

import com.inventory.product_manage.model.Product;
import com.inventory.product_manage.repository.ProductRepository;
import com.inventory.reports.dto.ProductReportRow;
import com.inventory.reports.dto.ReportResponse;
import com.inventory.transactions.model.TransactionType;
import com.inventory.transactions.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        List<Product> products = productRepository.findAll();
        List<ProductReportRow> rows = new ArrayList<>();

        for (Product product : products) {
            ProductReportRow row = new ProductReportRow();
            row.setProductId(product.getProductId());
            row.setSku(product.getSku());
            row.setName(product.getName());
            row.setCategory(product.getCategory() != null ? product.getCategory().getCategoryName() : null);
            row.setSupplier(product.getSupplier() != null ? product.getSupplier().getSupplierName() : null);
            row.setCurrentStock(product.getQuantity() == null ? 0 : product.getQuantity());
            row.setMinStockLevel(product.getMinStockLevel() == null ? 0 : product.getMinStockLevel());

            Integer totalIn = transactionRepository.sumQuantityByProductAndType(
                    product.getProductId(), TransactionType.STOCK_IN);
            Integer totalOut = transactionRepository.sumQuantityByProductAndType(
                    product.getProductId(), TransactionType.STOCK_OUT);

            row.setTotalStockIn(totalIn);
            row.setTotalStockOut(totalOut);

            rows.add(row);
        }

        ReportResponse response = new ReportResponse();
        response.setGeneratedAt(LocalDateTime.now());
        response.setRows(rows);
        return response;
    }
}
