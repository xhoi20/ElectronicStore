package com.electronicstore.service.serviceInterface;

import com.electronicstore.entity.Supplier;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

public interface ISupplierService {

    public ResponseEntity<Map<String, Object>> addSupplier(Map<String, String> supplierData);
    Iterable<Supplier> getAllSuppliers();
    void deleteSupplier(long id);
    public ResponseEntity<Map<String, Object>>  updateSupplier( Long id ,  Map<String, String> supplierData);
    Optional<Supplier> getSupplierById(Long id);
}
