package com.electronicstore.controller;

import com.electronicstore.service.PurchaseItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController
@RequestMapping("/api/purchase-items")
public class PurchaseItemController  extends BaseController{
    @Autowired
    private PurchaseItemService purchaseItemService;
    @PostMapping
    public ResponseEntity<Map<String, Object>> addPurchaseItem(@RequestBody Map<String, Object> purchaseData) {
    try{
    checkManagerRole();
    purchaseItemService.addPurchaseItem(purchaseData);
        return createSuccessResponse(purchaseItemService.addPurchaseItem(purchaseData), "Blerja u shtua me sukses", HttpStatus.CREATED);
    } catch (Exception e) {
        return handleException(e);
    }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePurchaseItem(@PathVariable Long id) {
        try {
            checkManagerRole();
            purchaseItemService.deletePurchaseItem(id);
            return createSuccessResponse(null, "Artikulli i blerjes u fshi me sukses", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePurchaseItem(@PathVariable Long id, @RequestBody Map<String, Object> purchaseItemData) {
        try {
            checkManagerRole();
            purchaseItemService.updatePurchaseItem(id,purchaseItemData );
            return createSuccessResponse(  purchaseItemService.updatePurchaseItem(id,purchaseItemData ), "Artikulli i blerjes u përditësua me sukses", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }
}
