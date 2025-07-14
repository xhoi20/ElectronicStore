package com.electronicstore.service;

import com.electronicstore.entity.*;
import com.electronicstore.repository.*;
import com.electronicstore.service.serviceInterface.IPurchaseItemService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PurchaseItemService implements IPurchaseItemService {
    @Autowired
    private PurchaseItemRepository purchaseItemRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;


private User validateUser(Long userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found."));
    System.out.println("User ID: " + user.getId() + ", Role: " + user.getRole() );
          //  ", Sector: " + (user.getSector() != null ? user.getSector().getId() : "null"));
    if (user.getRole() != UserRole.MANAGER && user.getRole() != UserRole.ADMIN) {
        throw new IllegalArgumentException("User must be a manager or admin.");
    }
    return user;
}
    @Transactional
    public PurchaseItem addPurchaseItem(Long userId, Long purchaseId, Long itemId, Integer quantity, Long invoiceId) {
        validateUser(userId);

        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("Purchase with ID " + purchaseId + " not found."));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item with ID " + itemId + " not found."));

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        Invoice invoice = null;
        if (invoiceId != null) {
            invoice = invoiceRepository.findById(invoiceId)
                    .orElseThrow(() -> new IllegalArgumentException("Invoice with ID " + invoiceId + " not found."));
        }

        PurchaseItem purchaseItem = PurchaseItem.builder()
                .purchase(purchase)
                .item(item)
                .quantity(quantity)
                .invoice(invoice)
                .build();

        return purchaseItemRepository.save(purchaseItem);
    }

    @Transactional
    public void deletePurchaseItem( Long id) {


        purchaseItemRepository.deleteById(id);
    }
    @Transactional
    public PurchaseItem updatePurchaseItem(Long userId, Long purchaseItemId, Integer quantity, Long invoiceId) {
        validateUser(userId);

        PurchaseItem purchaseItem = purchaseItemRepository.findById(purchaseItemId)
                .orElseThrow(() -> new IllegalArgumentException("Purchase item with ID " + purchaseItemId + " not found."));

        if (quantity != null && quantity > 0) {
            purchaseItem.setQuantity(quantity);
        } else if (quantity != null) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        if (invoiceId != null) {
            Invoice invoice = invoiceRepository.findById(invoiceId)
                    .orElseThrow(() -> new IllegalArgumentException("Invoice with ID " + invoiceId + " not found."));
            purchaseItem.setInvoice(invoice);
        }

        return purchaseItemRepository.save(purchaseItem);
    }
    @Transactional
    public Iterable<PurchaseItem> getAllPurchaseItems() {
    return purchaseItemRepository.findAll();

    }
    @Transactional
    public Optional<PurchaseItem> getPurchaseItem( Long purchaseItemId) {
       return purchaseItemRepository.findById(purchaseItemId);


    }
}
