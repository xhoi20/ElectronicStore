package com.electronicstore.repository;

import com.electronicstore.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase,Long> {
    List<Purchase> findByMenaxheriId(Long id);
    @Query("SELECT p FROM Purchase p JOIN FETCH p.furnitori JOIN FETCH p.menaxheri")
    List<Purchase> findAllWithRelations();
}
