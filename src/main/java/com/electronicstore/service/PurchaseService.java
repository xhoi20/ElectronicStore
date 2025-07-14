package com.electronicstore.service;

import com.electronicstore.entity.Purchase;
import com.electronicstore.entity.Supplier;
import com.electronicstore.entity.User;
import com.electronicstore.entity.UserRole;
import com.electronicstore.repository.PurchaseRepository;
import com.electronicstore.repository.SupplierRepository;
import com.electronicstore.repository.UserRepository;
import com.electronicstore.service.serviceInterface.IPurchaseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class PurchaseService implements IPurchaseService {
    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SupplierRepository supplierRepository;

private User validateUser(Long userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found."));
    System.out.println("User ID: " + user.getId() + ", Role: " + user.getRole() );
           // ", Sector: " + (user.getSector() != null ? user.getSector().getId() : "null"));
    if (user.getRole() != UserRole.MANAGER && user.getRole() != UserRole.ADMIN) {
        throw new IllegalArgumentException("User must be a manager or admin.");
    }
    return user;
}
    @Transactional
    public Purchase addPurchase(Long userId, LocalDateTime dataBlerjes, Long furnitorId, double totaliKostos, int sasia) {
        User user = validateUser(userId);
        Supplier furnitor = supplierRepository.findById(furnitorId)
                .orElseThrow(() -> new IllegalArgumentException("Supplier with ID " + furnitorId + " not found."));
        Purchase purchase = Purchase.builder()
                .dataBlerjes(dataBlerjes)
                .furnitori(furnitor)
                .menaxheri(user)
                .totaliKostos(totaliKostos)
                .sasia(sasia)
                .artikujt(new java.util.ArrayList<>())
                .build();

        return purchaseRepository.save(purchase);
    }



    @Transactional
    public void deletePurchase(Long id) {


        purchaseRepository.deleteById(id);
    }

@Transactional
public Purchase updatePurchase( Long purchaseId, LocalDateTime dataBlerjes, Long furnitorId, Double totaliKostos, Integer sasia) {
    Purchase purchase = purchaseRepository.findById(purchaseId)
            .orElseThrow(() -> new IllegalArgumentException("Purchase with ID " + purchaseId + " not found."));
//    User user = (userId != null) ? validateUser(userId) : purchase.getMenaxheri();
    Supplier furnitor = (furnitorId != null) ? supplierRepository.findById(furnitorId)
            .orElseThrow(() -> new IllegalArgumentException("Supplier with ID " + furnitorId + " not found.")) : purchase.getFurnitori();

    if (dataBlerjes != null) purchase.setDataBlerjes(dataBlerjes);
    if (furnitorId != null) purchase.setFurnitori(furnitor);
    //if (userId != null) purchase.setMenaxheri(user);
    if (totaliKostos != null) purchase.setTotaliKostos(totaliKostos);
    if (sasia != null) purchase.setSasia(sasia);

    return purchaseRepository.save(purchase);
}
@Transactional
    public Iterable<Purchase> getAllPurchases() {
    return purchaseRepository.findAll();
}
@Transactional
    public Optional<Purchase> getPurchaseById(Long purchaseId) {
    return purchaseRepository.findById(purchaseId);
}
}
