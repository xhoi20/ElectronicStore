package com.electronicstore.controller;
import com.electronicstore.entity.Supplier;
import com.electronicstore.service.serviceInterface.ISupplierService;
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
        checkManagerRole();
    return  supplierService.addSupplier(supplierData);

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
           checkManagerRole();
             supplierService.updateSupplier(id,supplierData);
            return createSuccessResponse(supplierService.updateSupplier(id,supplierData), "Supplier updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

}
