package com.electronicstore.controller;

import com.electronicstore.dto.ItemCreateDTO;
import com.electronicstore.dto.ItemUpdateDTO;

import com.electronicstore.entity.Item;
import com.electronicstore.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/item")
public class ItemController {
    @Autowired
    private ItemService itemService;
    @PostMapping
    public ResponseEntity<Item> createItem(@RequestParam Long userId, @RequestBody ItemCreateDTO dto){
        Item item = itemService.createItem(
                userId,
                dto.getEmri(),
                dto.getCategoryId(),
                dto.getCmimi(),
                dto.getSasia(),
                dto.getSupplierIds()
        );

        return ResponseEntity.ok(item);
    }
@PutMapping("/{itemId}")
public ResponseEntity<Item> updateItem(@RequestParam Long userId,      @PathVariable Long itemId, @RequestBody ItemUpdateDTO dto){
    Item updatedItem = itemService.updateItem(
            userId,
            itemId,
            dto.getEmri(),
            dto.getCmimi(),
            dto.getSasia()
    );
    return ResponseEntity.ok(updatedItem);
}

//    @DeleteMapping("/{itemId}")
//    public ResponseEntity<Void> deleteItem(
//            @RequestParam Long userId,
//            @PathVariable Long itemId) {
//        itemService.deleteItem(userId, itemId);
//        return ResponseEntity.noContent().build();
//    }
    @GetMapping("/{itemId}/stock-low")
    public ResponseEntity<Boolean> isStockLow(
            @PathVariable Long itemId,
            @RequestParam int threshold) {
        boolean low = itemService.isStockLow(itemId, threshold);
        return ResponseEntity.ok(low);
    }
    @PostMapping("/{itemId}/restock")
    public ResponseEntity<Item> restockItem(
            @RequestParam Long userId,
            @PathVariable Long itemId,
            @RequestParam int quantity) {
        Item updatedItem = itemService.restockItem(userId,itemId, quantity);
        return ResponseEntity.ok(updatedItem);
    }

}
