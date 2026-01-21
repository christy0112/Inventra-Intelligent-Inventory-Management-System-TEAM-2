package com.inventory.transactions.service;

import com.inventory.auth.model.User;
import com.inventory.auth.repository.UserRepository;
import com.inventory.product_manage.model.Product;
import com.inventory.product_manage.repository.ProductRepository;
import com.inventory.transactions.model.Transaction;
import com.inventory.transactions.model.TransactionType;
import com.inventory.transactions.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              ProductRepository productRepository,
                              UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public void log(TransactionType type, Long productId, int qty, Integer performedByUserId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Transaction tx = new Transaction();
        tx.setProduct(product);
        tx.setQuantity(qty);
        tx.setTransactionType(type);
        tx.setTransactionDate(LocalDateTime.now());

        if (performedByUserId != null) {
            Optional<User> performer = userRepository.findById(performedByUserId);
            performer.ifPresent(tx::setPerformedBy);
        }

        transactionRepository.save(tx);
    }

    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    public List<Transaction> byProduct(Long productId) {
        return transactionRepository.findByProduct_ProductId(productId);
    }
}
