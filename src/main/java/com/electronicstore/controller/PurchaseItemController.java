package com.electronicstore.controller;

import com.electronicstore.dto.PurchaseItemDTO;
import com.electronicstore.entity.Purchase;
import com.electronicstore.entity.PurchaseItem;
import com.electronicstore.service.PurchaseItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
        Long userId = purchaseData.get("userId") != null ?
                Long.valueOf(purchaseData.get("userId").toString()) : null;
        Long purchaseId = purchaseData.get("purchaseId") != null ?
                Long.valueOf(purchaseData.get("purchaseId").toString()) : null;
        Long itemId = purchaseData.get("itemId") != null ?
                Long.valueOf(purchaseData.get("itemId").toString()) : null;
        Long invoiceId = purchaseData.get("invoiceId") != null ?
                Long.valueOf(purchaseData.get("invoiceId").toString()) : null;

        Integer  quantity = purchaseData.get("quantity") != null ?
                Integer.parseInt(purchaseData.get("quantity").toString()) : null;
        if (userId == null) {
            return createErrorResponse("ID e perdoruesit nuk mund të jete bosh.", HttpStatus.BAD_REQUEST);
        }
        if (purchaseId == null) {
            return createErrorResponse("ID E BLERJEVE SMUNFD TE JETE BOSH.", HttpStatus.BAD_REQUEST);
        }
        if (itemId == null) {
            return createErrorResponse("ID e e produkteve nuk mund të jete bosh.", HttpStatus.BAD_REQUEST);
        }
        if (invoiceId == null) {
            return createErrorResponse("Futura nuk mund të jete bosh.", HttpStatus.BAD_REQUEST);
        }
        if (quantity== null) {
            return createErrorResponse("Sasia nuk mund të jete bosh.", HttpStatus.BAD_REQUEST);
        }

        PurchaseItem purchaseItem = purchaseItemService.addPurchaseItem(userId, purchaseId, itemId, quantity, invoiceId);
        return createSuccessResponse(purchaseItem, "Blerja u shtua me sukses", HttpStatus.CREATED);
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

            Long userId = purchaseItemData.get("userId") != null ?
                    Long.valueOf(purchaseItemData.get("userId").toString()) : null;
            Integer quantity = purchaseItemData.get("quantity") != null ?
                    Integer.parseInt(purchaseItemData.get("quantity").toString()) : null;
            Long invoiceId = purchaseItemData.get("invoiceId") != null ?
                    Long.valueOf(purchaseItemData.get("invoiceId").toString()) : null;

            if (userId == null) {
                return createErrorResponse("ID e perdoruesit nuk mund të jetë bosh.", HttpStatus.BAD_REQUEST);
            }

            PurchaseItem updatedPurchaseItem = purchaseItemService.updatePurchaseItem(userId, id, quantity, invoiceId);
            return createSuccessResponse(updatedPurchaseItem, "Artikulli i blerjes u përditësua me sukses", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }
}
