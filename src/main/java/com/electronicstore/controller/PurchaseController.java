package com.electronicstore.controller;

import com.electronicstore.dto.PurchaseCreateRequest;
import com.electronicstore.dto.PurchaseUpdateDTO;
import com.electronicstore.entity.Purchase;
import com.electronicstore.service.serviceInterface.IPurchaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
@RestController
@RequestMapping("api/purchase")
public class PurchaseController extends BaseController {
    @Autowired
    private IPurchaseService purchaseService;
    @PostMapping
public ResponseEntity<Map<String, Object>> addPurchase(@RequestBody Map<String, Object> purchaseData) {
    try{
        checkManagerRole();

        purchaseService.addPurchase(purchaseData);
        return createSuccessResponse(     purchaseService.addPurchase(purchaseData), "Blerja u shtua me sukses", HttpStatus.CREATED);
    } catch (Exception e) {
        return handleException(e);
    }
}
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePurchase(@PathVariable Long id) {
        try {
            checkManagerRole();
            purchaseService.deletePurchase(id);
            return createSuccessResponse(null, "Blerja u fshi me sukses", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePurchase(@PathVariable Long id, @RequestBody Map<String, Object> purchaseData) {
        try {
            checkManagerRole();
            purchaseService.updatePurchase(id,purchaseData);
            return createSuccessResponse( purchaseService.updatePurchase(id,purchaseData), "Blerja u perditesua me sukses", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }
}
