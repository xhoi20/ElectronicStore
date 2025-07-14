package com.electronicstore.repository;

import com.electronicstore.entity.PurchaseItem;
import org.springframework.data.repository.CrudRepository;

public interface PurchaseItemRepository extends CrudRepository<PurchaseItem, Long> {
    void deleteByItemId(Long id);
}
