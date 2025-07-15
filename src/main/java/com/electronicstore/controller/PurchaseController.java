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
        Long userId = purchaseData.get("userId") != null ?
                Long.valueOf(purchaseData.get("userId").toString()) : null;
        LocalDateTime dataBlerjes = purchaseData.get("dataBlerjes") != null ?
                LocalDateTime.parse(purchaseData.get("dataBlerjes").toString()) : null;
        Long furnitorId = purchaseData.get("furnitorId") != null ?
                Long.valueOf(purchaseData.get("furnitorId").toString()) : null;
        Double totaliKostos = purchaseData.get("totaliKostos") != null ?
                Double.parseDouble(purchaseData.get("totaliKostos").toString()) : null;
        Integer sasia = purchaseData.get("sasia") != null ?
                Integer.parseInt(purchaseData.get("sasia").toString()) : null;
        if (userId == null) {
            return createErrorResponse("ID e perdoruesit nuk mund të jete bosh.", HttpStatus.BAD_REQUEST);
        }
        if (dataBlerjes == null) {
            return createErrorResponse("Data e blerjes nuk mund të jete bosh.", HttpStatus.BAD_REQUEST);
        }
        if (furnitorId == null) {
            return createErrorResponse("ID e furnitorit nuk mund të jete bosh.", HttpStatus.BAD_REQUEST);
        }
        if (totaliKostos == null) {
            return createErrorResponse("Kosto totale nuk mund të jete bosh.", HttpStatus.BAD_REQUEST);
        }
        if (sasia == null) {
            return createErrorResponse("Sasia nuk mund të jete bosh.", HttpStatus.BAD_REQUEST);
        }

        Purchase purchase = purchaseService.addPurchase(userId, dataBlerjes, furnitorId, totaliKostos, sasia);
        return createSuccessResponse(purchase, "Blerja u shtua me sukses", HttpStatus.CREATED);
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

            LocalDateTime dataBlerjes = purchaseData.get("dataBlerjes") != null ?
                    LocalDateTime.parse(purchaseData.get("dataBlerjes").toString()) : null;
            Long furnitorId = purchaseData.get("furnitorId") != null ?
                    Long.valueOf(purchaseData.get("furnitorId").toString()) : null;
            Double totaliKostos = purchaseData.get("totaliKostos") != null ?
                    Double.parseDouble(purchaseData.get("totaliKostos").toString()) : null;
            Integer sasia = purchaseData.get("sasia") != null ?
                    Integer.parseInt(purchaseData.get("sasia").toString()) : null;

            Purchase updatedPurchase = purchaseService.updatePurchase(id, dataBlerjes, furnitorId, totaliKostos, sasia);
            return createSuccessResponse(updatedPurchase, "Blerja u perditesua me sukses", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }



}
