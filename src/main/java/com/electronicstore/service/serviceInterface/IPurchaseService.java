package com.electronicstore.service.serviceInterface;

import com.electronicstore.entity.Purchase;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

public interface IPurchaseService {
    public ResponseEntity<Map<String, Object>> addPurchase(Map<String, Object> purchaseData);
    public void deletePurchase( Long id);
    public ResponseEntity<Map<String, Object>> updatePurchase(Long id, Map<String, Object> purchaseData);
 Iterable<Purchase> getAllPurchases();
Optional<Purchase> getPurchaseById(Long purchaseId);
}
