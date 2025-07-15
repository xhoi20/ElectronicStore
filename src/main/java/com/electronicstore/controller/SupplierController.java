package com.electronicstore.controller;

import com.electronicstore.entity.Supplier;
import com.electronicstore.service.serviceInterface.ISupplierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/supplier")
public class SupplierController extends BaseController{
   @Autowired
   private ISupplierService supplierService;
    @GetMapping
    public Iterable<Supplier> getAllSuppliers() {

        return supplierService.getAllSuppliers();
    }
@PostMapping
public ResponseEntity<Map<String, Object>> addSupplier(  @RequestBody Map<String, String> supplierData) {
        try{
            String name = supplierData.get("name");
            if (name == null || name.trim().isEmpty()) {
                return createErrorResponse("Sector name is missing or empty", HttpStatus.BAD_REQUEST);
            }
            checkManagerRole();
            Supplier supplier = new Supplier();
            supplier.setName(name);
            supplier.setEmail(supplierData.get("email"));
            supplier.setAdresa(supplierData.get("adresa"));
            Supplier createdSupplier = supplierService.addSupplier(supplier);
            return createSuccessResponse(createdSupplier, "Supplier added successfully", HttpStatus.CREATED);
        }catch (Exception e) {
            return handleException(e);
        }


}
    @DeleteMapping("{id}")
public ResponseEntity<Map<String, Object>>deleteSupplier(@PathVariable Long id) {
       try {
           checkManagerRole();
           supplierService.deleteSupplier(id);
           return createSuccessResponse(null, "Supplier deleted successfully", HttpStatus.OK);
       } catch (Exception e) {
           return handleException(e);
       }
       }



    @PutMapping("{id}")
    public ResponseEntity<Map<String, Object>>  updateSupplier(@PathVariable Long id ,@RequestBody Map<String, String> supplierData) {

        try {
            String name = supplierData.get("name");
            if (name == null || name.trim().isEmpty()) {
                return createErrorResponse("Supplier name cannot be empty", HttpStatus.BAD_REQUEST);
            }
            String email = supplierData.get("email");
            String adresa = supplierData.get("adresa");

            checkManagerRole();

            Supplier supplier = new Supplier();
            supplier.setId(id);
            supplier.setName(name);
            supplier.setEmail(email);
            supplier.setAdresa(adresa);
            Supplier updatedSupplier = supplierService.updateSupplier(supplier);
            return createSuccessResponse(updatedSupplier, "Supplier updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

}
