package com.inventory.product_manage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.inventory.product_manage.model.*;

public interface CategoryRepository extends JpaRepository<Category, Long> {}

