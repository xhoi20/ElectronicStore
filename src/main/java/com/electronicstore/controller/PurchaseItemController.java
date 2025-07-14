package com.electronicstore.controller;

import com.electronicstore.dto.PurchaseItemDTO;
import com.electronicstore.entity.PurchaseItem;
import com.electronicstore.service.PurchaseItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchase-items")
public class PurchaseItemController {

    @Autowired
    private PurchaseItemService purchaseItemService;


    @PostMapping
public ResponseEntity<PurchaseItem> addPurchaseItem(@RequestBody PurchaseItemDTO purchaseItemDTO) {
    PurchaseItem purchaseItem = purchaseItemService.addPurchaseItem(
            purchaseItemDTO.getUserId(),
            purchaseItemDTO.getPurchaseId(),
            purchaseItemDTO.getItemId(),
            purchaseItemDTO.getQuantity(),
            purchaseItemDTO.getInvoiceId()
    );
    return ResponseEntity.ok(purchaseItem);
}
//    @DeleteMapping("/{purchaseItemId}")
//    public ResponseEntity<Void> removePurchaseItem(
//            @RequestParam Long userId,
//            @PathVariable Long purchaseItemId) {
//        purchaseItemService.removePurchaseItem(userId, purchaseItemId);
//        return ResponseEntity.noContent().build();
//    }

    @PutMapping("/{purchaseItemId}")
    public ResponseEntity<PurchaseItem> updatePurchaseItem(@RequestBody PurchaseItemDTO dto) {
        PurchaseItem purchaseItem = purchaseItemService.updatePurchaseItem(
                dto.getUserId(),
                dto.getPurchaseId(),
                dto.getQuantity(),
                dto.getInvoiceId()
        );
        return ResponseEntity.ok(purchaseItem);
    }
}
