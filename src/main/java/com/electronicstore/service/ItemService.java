package com.electronicstore.service;

import com.electronicstore.entity.*;
import com.electronicstore.repository.*;
import com.electronicstore.service.serviceInterface.IItemService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService implements IItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
private final UserRepository userRepository;
private final PurchaseItemRepository purchaseItemRepository;

    public Iterable<Item> getAllItems() {
        return itemRepository.findAll();
    }


    private User validateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found."));
        System.out.println("User ID: " + user.getId() + ", Role: " + user.getRole());
               // ", Sector: " + (user.getSector() != null ? user.getSector().getId() : "null"));
        if (user.getRole() != UserRole.MANAGER && user.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("User must be a manager or admin.");
        }
        return user;
    }
    @Transactional
    public Item createItem(Long userId, String emri, Long categoryId, BigDecimal cmimi, int sasia, List<Long> supplierIds) {
         validateUser(userId);

        Item item = new Item();
        item.setEmri(emri);
        item.setCmimi(cmimi);
        item.setSasia(sasia);

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found!"));
            item.setCategory(category);
        }

        if (supplierIds != null && !supplierIds.isEmpty()) {

            Iterable<Supplier> iterableSuppliers = supplierRepository.findAllById(supplierIds);
            List<Supplier> suppliers = new ArrayList<>();
            iterableSuppliers.forEach(suppliers::add);
            item.getSuppliers().addAll(suppliers);

        }

        return itemRepository.save(item);
    }
    @Transactional
    public Item updateItem(Long userId, Long itemId, String emri, BigDecimal cmimi, Integer sasia) {
         validateUser(userId);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found!"));

        if (emri != null) item.setEmri(emri);
        if (cmimi != null) item.setCmimi(cmimi);
        if (sasia != null) item.setSasia(sasia);

        return itemRepository.save(item);
    }
    @Transactional
    public void deleteItem( Long id) {

        purchaseItemRepository.deleteByItemId(id);

        itemRepository.deleteById(id);
    }
   @Transactional
    public boolean isStockLow(Long itemId, int threshold) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found!"));
        return item.getSasia() < threshold;
    }
    @Transactional
    public Item restockItem(Long userId,Long itemId, int additionalQuantity) {
        validateUser(userId);
        if (additionalQuantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found!"));
        item.setSasia(item.getSasia() + additionalQuantity);
        return itemRepository.save(item);
    }
@Transactional
    public Optional<Item>getItemById( Long id) {
        return itemRepository.findById(id);
}
}
