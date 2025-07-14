package com.electronicstore.service.serviceInterface;

import com.electronicstore.entity.Supplier;

import java.util.Optional;

public interface ISupplierService {

    Supplier addSupplier(Supplier suplier);
    Iterable<Supplier> getAllSuppliers();
    void deleteSupplier(long id);
    Supplier updateSupplier(Supplier suplier);
    Optional<Supplier> getSupplierById(Long id);
}
