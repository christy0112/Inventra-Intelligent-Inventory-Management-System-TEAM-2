package com.inventory.product_manage.controller;
import org.springframework.http.ResponseEntity;


import com.inventory.product_manage.model.Supplier;
import com.inventory.product_manage.repository.SupplierRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierRepository supplierRepository;

    public SupplierController(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    // ➕ Add Supplier
    @PostMapping
    public String addSupplier(@RequestBody Supplier supplier) {
        supplierRepository.save(supplier);
        return "Supplier Added Successfully";
    }
    @PutMapping("/{id}")
    public String updateSupplier(@PathVariable Long id, @RequestBody Supplier supplier) {
        Supplier s = supplierRepository.findById(id).orElseThrow();
        s.setSupplierName(supplier.getSupplierName());
        s.setContactNumber(supplier.getContactNumber());
        s.setEmail(supplier.getEmail());
        s.setAddress(supplier.getAddress());
        supplierRepository.save(s);
        return "Supplier Updated";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupplier(@PathVariable Long id) {
        try {
            supplierRepository.deleteById(id);
            return ResponseEntity.ok("Supplier deleted successfully");
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Cannot delete supplier. Products are using this supplier.");
        }
    }


    // 📋 Get All Suppliers
    @GetMapping
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }
}
