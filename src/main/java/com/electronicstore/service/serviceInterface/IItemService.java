package com.electronicstore.service.serviceInterface;

import com.electronicstore.entity.Item;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IItemService {
   public ResponseEntity<Map<String, Object>> createItem(Map<String, Object> itemData);

   public ResponseEntity<Map<String, Object>> updateItem( Long id, Map<String, Object> itemData);
    public void deleteItem(Long id);
    Iterable<Item> getAllItems();

    public boolean isStockLow(Long itemId, int threshold);
   public ResponseEntity<Map<String, Object>> restockItem(Long itemId, Map<String, Object> itemData);
Optional<Item> getItemById(Long id);



}
