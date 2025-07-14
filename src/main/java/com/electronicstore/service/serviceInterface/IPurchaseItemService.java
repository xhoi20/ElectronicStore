package com.electronicstore.service.serviceInterface;

import com.electronicstore.entity.PurchaseItem;

import java.util.Optional;

public interface IPurchaseItemService {
    PurchaseItem addPurchaseItem(Long userId, Long purchaseId, Long itemId, Integer quantity, Long invoiceId);
    public void deletePurchaseItem( Long Id);
    PurchaseItem updatePurchaseItem(Long userId, Long purchaseItemId, Integer quantity, Long invoiceId);
Iterable<PurchaseItem> getAllPurchaseItems();
Optional<PurchaseItem> getPurchaseItem(Long purchaseItemId);
}
