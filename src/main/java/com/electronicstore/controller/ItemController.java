package com.electronicstore.controller;
import com.electronicstore.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
             itemService.createItem(itemData);
            return createSuccessResponse( itemService.createItem(itemData), "Artikulli u shtua me sukses", HttpStatus.CREATED);
        } catch (Exception e) {
            return handleException(e);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateItem(@PathVariable Long id, @RequestBody Map<String, Object> itemData) {
        try {
            checkManagerRole();

            return itemService.updateItem( id,itemData );

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
            return   itemService.restockItem(itemId,itemData );
        } catch (Exception e) {
            return handleException(e);
        }
    }

}
