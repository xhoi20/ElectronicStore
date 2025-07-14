package com.electronicstore.repository;

import com.electronicstore.entity.Purchase;
import org.springframework.data.repository.CrudRepository;

public interface PurchaseRepository extends CrudRepository<Purchase,Long> {
}
