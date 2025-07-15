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
import java.util.ArrayList;
import java.util.Optional;

import static com.electronicstore.entity.UserRole.MANAGER;


@Service
public class PurchaseService extends BaseService implements IPurchaseService {
    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SupplierRepository supplierRepository;




@Transactional
public Purchase addPurchase(Long userId, LocalDateTime dataBlerjes,Long furnitorId,double totaliKostos,int sasia) {
    User authenticatedUser=getAuthenticatedUser();
    if (dataBlerjes == null) {
        throw new IllegalArgumentException("Data e blerjes nuk mund të jete bosh.");
    }
    if (furnitorId == null) {
        throw new IllegalArgumentException("ID e furnitorit nuk mund të jete bosh.");
    }
    if (totaliKostos < 0) {
        throw new IllegalArgumentException("Kosto totale nuk mund të jete negative.");
    }
    if (sasia <= 0) {
        throw new IllegalArgumentException("Sasia duhet te jete me e madhe se zero.");
    }
    Supplier furnitor = supplierRepository.findById(furnitorId)
            .orElseThrow(() -> new IllegalArgumentException("Furnitori me ID " + furnitorId + " nuk gjendet."));
    Purchase purchase=Purchase.builder()
            .dataBlerjes(dataBlerjes)
            .furnitori(furnitor)
            .menaxheri(authenticatedUser)
            .totaliKostos(totaliKostos)
            .sasia(sasia)
            .artikujt(new ArrayList<>())
            .build();
    return purchaseRepository.save(purchase);
}

    @Transactional
    public void deletePurchase(Long id) {
        User authenticatedUser = getAuthenticatedUser(); // Ensure only managers can delete purchases
        Optional<Purchase> purchaseOptional = purchaseRepository.findById(id);
        if (purchaseOptional.isEmpty()) {
            throw new IllegalArgumentException("Blerja me ID "+id + " nuk gjendet.");
        }
        Purchase purchase = purchaseOptional.get();
        purchase.getArtikujt().clear();
        purchaseRepository.save(purchase);
        purchaseRepository.deleteById(id);

        purchaseRepository.deleteById(id);
    }

@Transactional
public Purchase updatePurchase(Long purchaseId,  LocalDateTime dataBlerjes, Long furnitorId, Double totaliKostos, Integer sasia) {
    User authenticatedUser=getAuthenticatedUser();
    Optional<Purchase> purchaseOptional = purchaseRepository.findById(purchaseId);
    if (purchaseOptional.isEmpty()) {
        throw new IllegalArgumentException("Blerja me ID " + purchaseId + " nuk gjendet.");
    }

    Purchase purchase = purchaseOptional.get();
    if (furnitorId != null) {
        Supplier furnitor = supplierRepository.findById(furnitorId)
                .orElseThrow(() -> new IllegalArgumentException("Furnitori me ID " + furnitorId + " nuk gjendet."));
        purchase.setFurnitori(furnitor);
    }
    if (dataBlerjes != null) {
        purchase.setDataBlerjes(dataBlerjes);
    }
    if (totaliKostos != null) {
        if (totaliKostos < 0) {
            throw new IllegalArgumentException("Kosto totale nuk mund të jetë negative.");
        }
        purchase.setTotaliKostos(totaliKostos);
    }
    if (sasia != null) {
        if (sasia <= 0) {
            throw new IllegalArgumentException("Sasia duhet të jetë më e madhe se zero.");
        }
        purchase.setSasia(sasia);
    }

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
