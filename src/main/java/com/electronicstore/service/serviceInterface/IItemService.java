package com.electronicstore.service.serviceInterface;

import com.electronicstore.entity.Item;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IItemService {
 Item createItem(Long userId, String emri, Long categoryId, BigDecimal cmimi, int sasia, List<Long> supplierIds);
    Item updateItem(Long userId, Long itemId, String emri, BigDecimal cmimi, Integer sasia);
    public void deleteItem(Long id);
    Iterable<Item> getAllItems();

    public boolean isStockLow(Long itemId, int threshold);
    Item restockItem(Long itemId, int additionalQuantity);
Optional<Item> getItemById(Long id);



}
