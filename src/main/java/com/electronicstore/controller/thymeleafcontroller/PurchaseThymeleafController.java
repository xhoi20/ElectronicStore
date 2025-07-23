package com.electronicstore.controller.thymeleafcontroller;

import com.electronicstore.entity.Purchase;

import com.electronicstore.service.BaseService;
import com.electronicstore.service.serviceInterface.IPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/purchases")
public class PurchaseThymeleafController extends BaseService {
    @Autowired
    private IPurchaseService purchaseService;
    @GetMapping
    public String getAllPurchases(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            String role = authentication.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .findFirst()
                    .map(auth -> auth.replace("ROLE_", ""))
                    .orElse("");

            model.addAttribute("email", email);
            model.addAttribute("role", role);
            Iterable<Purchase> purchases = purchaseService.getAllPurchases();
            model.addAttribute("purchases", purchases);
        }
        return "purchases-list";
    }
    @GetMapping("/add")
    public String getAddPurchase(Model model) {
        model.addAttribute("purchase", new Purchase());
        return "purchases-form";
    }
    @PostMapping("/save")
    public String savePurchase(@ModelAttribute("purchase") Purchase purchase, RedirectAttributes redirectAttributes) {
        try {
            Map<String, Object> purchaseData = new HashMap<>();
            purchaseData.put("dataBlerjes", purchase.getDataBlerjes() != null ? purchase.getDataBlerjes().toString() : null);
            purchaseData.put("furnitorId", purchase.getFurnitori() != null ? purchase.getFurnitori().getId() : null);
            purchaseData.put("totaliKostos", purchase.getTotaliKostos());
            purchaseData.put("sasia", purchase.getSasia());
            purchaseData.put("userId", getAuthenticatedUser().getId());
        purchaseService.addPurchase(purchaseData);
    } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/purchases";
    }

    @GetMapping("/edit/{id}")
    public String getEditPurchase(@PathVariable Long id, Model model) {
        Purchase purchase = id != null ? purchaseService.getPurchaseById(id)
                .orElse(new Purchase()) : new Purchase();
        model.addAttribute("purchase", purchase);
        return "edit-purchases";
    }
    @PostMapping("/edit/{id}")
    public String updatePurchase(@PathVariable Long id,@ModelAttribute("purchase") Purchase purchase,RedirectAttributes redirectAttributes) {
        try {
            Map<String, Object> purchaseData = new HashMap<>();
            purchaseData.put("dataBlerjes", purchase.getDataBlerjes() != null ? purchase.getDataBlerjes().toString() : null);
            purchaseData.put("furnitorId", purchase.getFurnitori() != null ? purchase.getFurnitori().getId() : null);
            purchaseData.put("totaliKostos", purchase.getTotaliKostos());
            purchaseData.put("sasia", purchase.getSasia());
            purchaseData.put("userId", getAuthenticatedUser().getId());
            purchaseService.updatePurchase(id,purchaseData );
            redirectAttributes.addFlashAttribute("message", "Purchase updated successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/purchases";
    }
    @PostMapping("/delete/{id}")
    public String deletePurchaseItem(@PathVariable Long id,  RedirectAttributes redirectAttributes) {
        try {
            purchaseService.deletePurchase(id);
            redirectAttributes.addFlashAttribute("message", "Purchase deleted successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/purchases";
    }
}
