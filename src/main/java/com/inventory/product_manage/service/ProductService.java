package com.inventory.product_manage.service;

import com.inventory.product_manage.model.Category;
import com.inventory.product_manage.model.Product;
import com.inventory.product_manage.model.Supplier;
import com.inventory.product_manage.repository.CategoryRepository;
import com.inventory.product_manage.repository.ProductRepository;
import com.inventory.product_manage.repository.SupplierRepository;
import org.springframework.stereotype.Service;

/**
 * Product Service - Module 2: Product/Inventory Management
 * Handles product CRUD operations and business logic
 * Matches pseudocode: ProductService
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          SupplierRepository supplierRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
    }

    /**
     * Add a new product if the SKU is unique.
     * Matches pseudocode:
     * FUNCTION addProduct(productData)
     *     IF product SKU not exists THEN
     *         save product
     *     ELSE
     *         RETURN "Duplicate Product"
     *     END IF
     * END FUNCTION
     */
    public String addProduct(Product productData) {

        // Duplicate check by SKU
        if (productData.getSku() != null &&
                productRepository.findBySku(productData.getSku()).isPresent()) {
            return "Duplicate Product";
        }

        // Attach Category if ID is provided
        if (productData.getCategory() != null &&
                productData.getCategory().getCategoryId() != null) {
            Long catId = productData.getCategory().getCategoryId();
            Category category = categoryRepository.findById(catId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            productData.setCategory(category);
        }

        // Attach Supplier if ID is provided
        if (productData.getSupplier() != null &&
                productData.getSupplier().getSupplierId() != null) {
            Long supId = productData.getSupplier().getSupplierId();
            Supplier supplier = supplierRepository.findById(supId)
                    .orElseThrow(() -> new RuntimeException("Supplier not found"));
            productData.setSupplier(supplier);
        }

        // Default values for stock & threshold
        if (productData.getQuantity() == null) {
            productData.setQuantity(0);
        }
        if (productData.getMinStockLevel() == null) {
            productData.setMinStockLevel(0);
        }

        productRepository.save(productData);
        return "Product Added Successfully";
    }
}


