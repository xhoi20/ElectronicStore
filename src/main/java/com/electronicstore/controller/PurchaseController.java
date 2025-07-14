package com.electronicstore.controller;

import com.electronicstore.dto.PurchaseCreateRequest;
import com.electronicstore.dto.PurchaseUpdateDTO;
import com.electronicstore.entity.Purchase;
import com.electronicstore.service.serviceInterface.IPurchaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/purchase")
public class PurchaseController {
    @Autowired
    private IPurchaseService purchaseService;

@PostMapping
public ResponseEntity<Purchase> createPurchase(@RequestBody PurchaseCreateRequest request) {
    Purchase purchase = purchaseService.addPurchase(
            request.getUserId(),
            request.getDataBlerjes(),
            request.getFurnitorId(),
            request.getTotaliKostos(),
            request.getSasia()
    );
    return ResponseEntity.ok(purchase);
}

//    @DeleteMapping("/{purchaseId}")
//    public ResponseEntity<Void> removePurchase(
//            @RequestParam Long userId,
//            @PathVariable Long purchaseId) {
//        purchaseService.removePurchase(userId, purchaseId);
//        return ResponseEntity.noContent().build();
//    }


//@PutMapping("/{purchaseId}")
//public ResponseEntity<Purchase> updatePurchase(
//        @PathVariable Long purchaseId,
//        @RequestBody PurchaseUpdateDTO purchaseUpdateDTO) {
//    Purchase purchase = purchaseService.updatePurchase(
//            purchaseUpdateDTO.getUserId(),
//            purchaseId,
//            purchaseUpdateDTO.getDataBlerjes(),
//            purchaseUpdateDTO.getFurnitorId(),
//            purchaseUpdateDTO.getTotaliKostos(),
//            purchaseUpdateDTO.getSasia()
//    );
//    return ResponseEntity.ok(purchase);
//}
}
