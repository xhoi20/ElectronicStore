package com.electronicstore.service.serviceInterface;

import com.electronicstore.entity.Purchase;

import java.time.LocalDateTime;
import java.util.Optional;

public interface IPurchaseService {
    Purchase addPurchase(Long userId, LocalDateTime dataBlerjes, Long furnitorId, double totaliKostos, int sasia);
    public void deletePurchase( Long id);
    Purchase updatePurchase( Long purchaseId, LocalDateTime dataBlerjes, Long furnitorId, Double totaliKostos, Integer sasia);
 Iterable<Purchase> getAllPurchases();
Optional<Purchase> getPurchaseById(Long purchaseId);
}
