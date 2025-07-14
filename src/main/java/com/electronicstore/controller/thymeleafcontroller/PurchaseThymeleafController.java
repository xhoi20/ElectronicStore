package com.electronicstore.controller.thymeleafcontroller;

import com.electronicstore.entity.Purchase;

import com.electronicstore.service.serviceInterface.IPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/purchases")
public class PurchaseThymeleafController {
    @Autowired
    private IPurchaseService purchaseService;
    @GetMapping
    public String getAllPurchases(Model model) {
        Iterable<Purchase>purchases=purchaseService.getAllPurchases();
        model.addAttribute("purchases", purchases);
        return "purchases-list";
    }
    @GetMapping("/add")
    public String getAddPurchase(Model model) {
        model.addAttribute("purchase", new Purchase());
        return "purchases-form";
    }
    @PostMapping("/save")
    public String savePurchase(@ModelAttribute("purchase") Purchase purchase,@RequestParam Long userId, RedirectAttributes redirectAttributes) {
        try {
        purchaseService.addPurchase(userId,purchase.getDataBlerjes(),purchase.getFurnitori().getId(),purchase.getTotaliKostos(), purchase.getSasia());
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
            purchaseService.updatePurchase(id, purchase.getDataBlerjes(),  purchase.getFurnitori().getId(), purchase.getTotaliKostos(),purchase.getSasia() );
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
