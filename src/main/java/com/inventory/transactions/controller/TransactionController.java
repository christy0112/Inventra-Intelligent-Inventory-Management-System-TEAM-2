package com.inventory.transactions.controller;

import com.inventory.transactions.model.Transaction;
import com.inventory.transactions.model.TransactionRequest;
import com.inventory.transactions.model.TransactionType;
import com.inventory.transactions.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<Transaction> all() {
        return transactionService.getAll();
    }

    @GetMapping("/product/{productId}")
    public List<Transaction> byProduct(@PathVariable Long productId) {
        return transactionService.byProduct(productId);
    }

    @PostMapping("/log")
    public ResponseEntity<String> log(@RequestBody TransactionRequest request) {
        TransactionType type = TransactionType.valueOf(request.getTransactionType().toUpperCase());
        transactionService.log(type, request.getProductId(), request.getQuantity(), request.getPerformedByUserId());
        return ResponseEntity.ok("Transaction logged");
    }
}
