package com.electronicstore.controller;



import com.electronicstore.entity.Item;

import com.electronicstore.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/item")
public class ItemController extends BaseController {
    @Autowired
    private ItemService itemService;
    @PostMapping
    public ResponseEntity<Map<String, Object>> createItem(@RequestBody Map<String, Object> itemData) {
        try {
            checkManagerRole();
            Long userId = itemData.get("userId") != null ?
                    Long.valueOf(itemData.get("userId").toString()) : null;
            String emri = itemData.get("emri") != null ?
                    itemData.get("emri").toString() : null;
            Long categoryId = itemData.get("categoryId") != null ?
                    Long.valueOf(itemData.get("categoryId").toString()) : null;
            BigDecimal cmimi = itemData.get("cmimi") != null ?
                    new BigDecimal(itemData.get("cmimi").toString()) : null;
            Integer sasia = itemData.get("sasia") != null ?
                    Integer.parseInt(itemData.get("sasia").toString()) : null;
            List<Long> supplierIds = itemData.get("supplierIds") != null ?
                    (List<Long>) itemData.get("supplierIds") : null;

            if (userId == null) {
                return createErrorResponse("ID e perdoruesit nuk mund të jete bosh.", HttpStatus.BAD_REQUEST);
            }
            if (emri == null || emri.trim().isEmpty()) {
                return createErrorResponse("Emri i artikullit nuk mund te jete bosh.", HttpStatus.BAD_REQUEST);
            }
            if (cmimi == null) {
                return createErrorResponse("Çmimi nuk mund të jete bosh.", HttpStatus.BAD_REQUEST);
            }
            if (sasia == null) {
                return createErrorResponse("Sasia nuk mund të jete bosh.", HttpStatus.BAD_REQUEST);
            }

            Item item = itemService.createItem(userId, emri, categoryId, cmimi, sasia, supplierIds);
            return createSuccessResponse(item, "Artikulli u shtua me sukses", HttpStatus.CREATED);
        } catch (Exception e) {
            return handleException(e);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateItem(@PathVariable Long id, @RequestBody Map<String, Object> itemData) {
        try {
            checkManagerRole();
            Long userId = itemData.get("userId") != null ?
                    Long.valueOf(itemData.get("userId").toString()) : null;
            String emri = itemData.get("emri") != null ?
                    itemData.get("emri").toString() : null;
            BigDecimal cmimi = itemData.get("cmimi") != null ?
                    new BigDecimal(itemData.get("cmimi").toString()) : null;
            Integer sasia = itemData.get("sasia") != null ?
                    Integer.parseInt(itemData.get("sasia").toString()) : null;

            if (userId == null) {
                return createErrorResponse("ID e perdoruesit nuk mund të jete bosh.", HttpStatus.BAD_REQUEST);
            }

            Item updatedItem = itemService.updateItem(userId, id, emri, cmimi, sasia);
            return createSuccessResponse(updatedItem, "Artikulli u perditesua me sukses", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteItem(@PathVariable Long id) {
        try {
            checkManagerRole();
            itemService.deleteItem(id);
            return createSuccessResponse(null, "Artikulli u fshi me sukses", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @GetMapping("/stock-low/{itemId}/{threshold}")
    public ResponseEntity<Map<String, Object>> isStockLow(@PathVariable Long itemId, @PathVariable int threshold) {
        try {
            checkManagerRole();
            boolean isLow = itemService.isStockLow(itemId, threshold);
            return createSuccessResponse(isLow, "Gjendja e stokut u kontrollua me sukses", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }
    @PutMapping("/restock/{itemId}")
    public ResponseEntity<Map<String, Object>> restockItem(@PathVariable Long itemId, @RequestBody Map<String, Object> itemData) {
        try {
            checkManagerRole();
            Integer additionalQuantity = itemData.get("additionalQuantity") != null ?
                    Integer.parseInt(itemData.get("additionalQuantity").toString()) : null;

            if (additionalQuantity == null) {
                return createErrorResponse("Sasia shtese nuk mund te jete bosh.", HttpStatus.BAD_REQUEST);
            }

            Item restockedItem = itemService.restockItem(itemId, additionalQuantity);
            return createSuccessResponse(restockedItem, "Artikulli u rimbush me sukses", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

}
