package com.electronicstore.service.serviceInterface;

import com.electronicstore.entity.PurchaseItem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.Optional;

public interface IPurchaseItemService {
    public ResponseEntity<Map<String, Object>> addPurchaseItem(@RequestBody Map<String, Object> purchaseData);
    public void deletePurchaseItem( Long Id);
    public ResponseEntity<Map<String, Object>> updatePurchaseItem( Long purchaseItemId, Map<String, Object> purchaseItemData);
Iterable<PurchaseItem> getAllPurchaseItems();
Optional<PurchaseItem> getPurchaseItem(Long purchaseItemId);
}
