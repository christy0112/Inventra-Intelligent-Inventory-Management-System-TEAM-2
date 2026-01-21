package com.inventory.product_manage.repository;

import com.inventory.product_manage.model.Inventory;
import com.inventory.product_manage.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProduct(Product product);
}
