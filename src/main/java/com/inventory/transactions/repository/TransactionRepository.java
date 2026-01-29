package com.inventory.transactions.repository;

import com.inventory.transactions.model.Transaction;
import com.inventory.transactions.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByProduct_ProductId(Long productId);

    @Query("select coalesce(sum(t.quantity),0) from Transaction t where t.product.productId = :productId and t.transactionType = :type")
    Integer sumQuantityByProductAndType(@Param("productId") Long productId,
                                        @Param("type") TransactionType type);

    @Query("""
            select coalesce(sum(t.quantity),0)
            from Transaction t
            where t.product.productId = :productId
              and t.transactionType = :type
              and t.transactionDate between :from and :to
            """)
    Integer sumQuantityByProductAndTypeAndDateRange(@Param("productId") Long productId,
                                                    @Param("type") TransactionType type,
                                                    @Param("from") LocalDateTime from,
                                                    @Param("to") LocalDateTime to);
}
