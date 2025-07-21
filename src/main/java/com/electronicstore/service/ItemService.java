package com.electronicstore.service;
import com.electronicstore.entity.*;
import com.electronicstore.repository.*;
import com.electronicstore.service.serviceInterface.IItemService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class ItemService extends BaseService implements IItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
private final PurchaseItemRepository purchaseItemRepository;

    public Iterable<Item> getAllItems() {

        return itemRepository.findAll();
    }
public ResponseEntity<Map<String, Object>> createItem( Map<String, Object> itemData) {
   getAuthenticatedUser();
    try {


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


        if (emri == null || emri.trim().isEmpty()) {
            return createErrorResponse("Emri i artikullit nuk mund te jete bosh.", HttpStatus.BAD_REQUEST);
        }
        if (cmimi == null) {
            return createErrorResponse("Çmimi nuk mund të jete bosh.", HttpStatus.BAD_REQUEST);
        }
        if (sasia == null) {
            return createErrorResponse("Sasia nuk mund të jete bosh.", HttpStatus.BAD_REQUEST);
        }
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
        Item items = itemRepository.save(item);
        return createSuccessResponse(items, "Artikulli u shtua me sukses", HttpStatus.CREATED);
    } catch (Exception e) {
        return handleException(e);
    }
}
public ResponseEntity<Map<String, Object>> updateItem( Long id, Map<String, Object> itemData) {
   getAuthenticatedUser();
    try {


        String emri = itemData.get("emri") != null ?
                itemData.get("emri").toString() : null;
        BigDecimal cmimi = itemData.get("cmimi") != null ?
                new BigDecimal(itemData.get("cmimi").toString()) : null;
        Integer sasia = itemData.get("sasia") != null ?
                Integer.parseInt(itemData.get("sasia").toString()) : null;


        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found!"));

        if (emri != null) item.setEmri(emri);
        if (cmimi != null) item.setCmimi(cmimi);
       if (sasia != null) item.setSasia(sasia);

        Item updatedItem = itemRepository.save(item);
        return createSuccessResponse(updatedItem, "Artikulli u perditesua me sukses", HttpStatus.OK);
    } catch (Exception e) {
        return handleException(e);
    }
}
    @Transactional
    public void deleteItem( Long id) {

        purchaseItemRepository.deleteByItemId(id);

        itemRepository.deleteById(id);
    }
   @Transactional
    public boolean isStockLow(Long itemId, int threshold) {
getAuthenticatedUser();
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found!"));
        return item.getSasia() < threshold;
    }
public ResponseEntity<Map<String, Object>> restockItem(Long itemId, Map<String, Object> itemData) {
   getAuthenticatedUser();
    try {

        Integer additionalQuantity = itemData.get("additionalQuantity") != null ?
                Integer.parseInt(itemData.get("additionalQuantity").toString()) : null;

        if (additionalQuantity == null) {
            return createErrorResponse("Sasia shtese nuk mund te jete bosh.", HttpStatus.BAD_REQUEST);
        }
        if (additionalQuantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found!"));
        item.setSasia(item.getSasia() + additionalQuantity);
        Item restockedItem = itemRepository.save(item);
        return createSuccessResponse(restockedItem, "Artikulli u rimbush me sukses", HttpStatus.OK);
    } catch (Exception e) {
        return handleException(e);
    }
}
@Transactional
    public Optional<Item>getItemById( Long id) {
        return itemRepository.findById(id);
}
}
