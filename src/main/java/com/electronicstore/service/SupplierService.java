package com.electronicstore.service;

import com.electronicstore.entity.Supplier;
import com.electronicstore.entity.User;
import com.electronicstore.repository.SupplierRepository;
import com.electronicstore.service.serviceInterface.ISupplierService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.Optional;

@Service
public class SupplierService  extends BaseService implements ISupplierService {
    @Autowired
    private SupplierRepository supplierRepository;
    @Transactional
    public Iterable<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }


@Transactional
public ResponseEntity<Map<String, Object>> addSupplier( Map<String, String> supplierData) {

    getAuthenticatedUser();
    try{
        String name = supplierData.get("name");
        if (name == null || name.trim().isEmpty()) {
            return createErrorResponse("Sector name is missing or empty", HttpStatus.BAD_REQUEST);
        }

        Supplier supplier = new Supplier();
        supplier.setName(name);
        supplier.setEmail(supplierData.get("email"));
        supplier.setAdresa(supplierData.get("adresa"));
        Supplier createdSupplier =supplierRepository.save(supplier);
        return createSuccessResponse(createdSupplier, "Supplier added successfully", HttpStatus.CREATED);
    }catch (Exception e) {
        return handleException(e);
    }


}
@Transactional
public ResponseEntity<Map<String, Object>>  updateSupplier( Long id ,  Map<String, String> supplierData) {
    getAuthenticatedUser();
    try {
        String name = supplierData.get("name");
        if (name == null || name.trim().isEmpty()) {
            return createErrorResponse("Supplier name cannot be empty", HttpStatus.BAD_REQUEST);
        }
        String email = supplierData.get("email");
        String adresa = supplierData.get("adresa");
        Supplier supplier = new Supplier();
        supplier.setId(id);
        supplier.setName(name);
        supplier.setEmail(email);
        supplier.setAdresa(adresa);
        Supplier updatedSupplier = supplierRepository.save(supplier);
        return createSuccessResponse(updatedSupplier, "Supplier updated successfully", HttpStatus.OK);
    } catch (Exception e) {
        return handleException(e);
    }
}
    @Transactional
    public void deleteSupplier(long id) {
         getAuthenticatedUser();
        Optional<Supplier> supplierOptional= supplierRepository.findById(id);
        if (supplierOptional.isEmpty()) {
            throw new IllegalArgumentException("Supplier not found with id: " + id);
        }

        Supplier supplier = supplierOptional.get();
        supplier.getItems().forEach(item -> item.getSuppliers().remove(supplier));
        supplier.getItems().clear();
        supplierRepository.deleteById(id);
    }

    @Transactional
    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }
}
