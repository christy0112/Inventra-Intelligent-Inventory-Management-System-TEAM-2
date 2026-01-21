package com.inventory.product_manage.repository;

import com.inventory.product_manage.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
