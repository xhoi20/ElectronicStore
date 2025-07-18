package com.electronicstore.repository;

import com.electronicstore.entity.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
    void deleteByItemId(Long id);
}
