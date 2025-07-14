package com.electronicstore.service;

import com.electronicstore.entity.Supplier;
import com.electronicstore.entity.User;
import com.electronicstore.repository.SupplierRepository;
import com.electronicstore.service.serviceInterface.ISupplierService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SupplierService  implements ISupplierService {
    @Autowired
    private SupplierRepository supplierRepository;
    @Transactional
    public Iterable<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Transactional
    public Supplier addSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }
    @Transactional
    public Supplier updateSupplier(Supplier supplier) {
        Optional<Supplier> supplierOptional= supplierRepository.findById(supplier.getId());
   if (!supplierOptional.isPresent()) {
           throw new IllegalArgumentException("produkti me ID" + supplier.getId() + "nuk gjendet");
    }

        Supplier existingSupplier = supplierOptional.get();
        existingSupplier.setName(supplier.getName());
        existingSupplier.setEmail(supplier.getEmail());
        existingSupplier.setAdresa(supplier.getAdresa());
        return supplierRepository.save(existingSupplier);
    }
    @Transactional
    public void deleteSupplier(long id) {

        supplierRepository.deleteById(id);
    }

    @Transactional
    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }
}
