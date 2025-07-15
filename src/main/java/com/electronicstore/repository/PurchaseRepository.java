package com.electronicstore.repository;

import com.electronicstore.entity.Purchase;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PurchaseRepository extends CrudRepository<Purchase,Long> {
    List<Purchase> findByMenaxheriId(Long id);
}
