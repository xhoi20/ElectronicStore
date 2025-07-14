package com.electronicstore.controller;

import com.electronicstore.entity.Supplier;
import com.electronicstore.service.serviceInterface.ISupplierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/supplier")
public class SupplierController {
   @Autowired
   private ISupplierService supplierService;
    @GetMapping
    public Iterable<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }
@PostMapping
    public Supplier addSupplier(@Valid @RequestBody Supplier supplier) {
        return supplierService.addSupplier(supplier);

}
    @DeleteMapping("{id}")
    public void deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);

    }
    @PutMapping("{id}")
    public Supplier updateSupplier(@PathVariable Long id ,@Valid @RequestBody Supplier supplier) {
        supplier.setId(id);
       return supplierService.updateSupplier(supplier);

    }

}
