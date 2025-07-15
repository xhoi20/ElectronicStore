package com.electronicstore.controller.thymeleafcontroller;

import com.electronicstore.entity.Item;
import com.electronicstore.service.serviceInterface.ICategoryService;
import com.electronicstore.service.serviceInterface.IItemService;
import com.electronicstore.service.serviceInterface.ISupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/items")
public class ItemThymeleafController {

    @Autowired
    private IItemService itemService;

    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private ISupplierService supplierService;

    @GetMapping
    public String getAllItems(Model model) {
        Iterable<Item> items = itemService.getAllItems();
        model.addAttribute("items", items);
        return "items-list";
    }

    @GetMapping("/add")
    public String getAddItem(Model model) {
        model.addAttribute("item", new Item());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        return "items-form";
    }

    @PostMapping("/save")
    public String saveItem(@ModelAttribute("item") Item item,
                           @RequestParam Long userId,
                           @RequestParam(required = false) Long categoryId,
                           @RequestParam(required = false) List<Long> supplierIds,
                           RedirectAttributes redirectAttributes) {
        try {
            itemService.createItem(userId, item.getEmri(), categoryId, item.getCmimi(), item.getSasia(), supplierIds);
            redirectAttributes.addFlashAttribute("message", "Item created successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/items";
    }

    @GetMapping("/edit/{id}")
    public String getEditItem(@PathVariable Long id, Model model) {
        Item item = itemService.getItemById(id)
                .orElse(new Item());
        model.addAttribute("item", item);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        return "edit-items";
    }

    @PostMapping("/edit/{id}")
    public String updateItem(@PathVariable Long id,
                             @ModelAttribute("item") Item item,
                             @RequestParam Long userId,
                             RedirectAttributes redirectAttributes) {
        try {
            itemService.updateItem(userId, id, item.getEmri(), item.getCmimi(), item.getSasia());
            redirectAttributes.addFlashAttribute("message", "Item updated successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/items";
    }

    @PostMapping("/delete/{id}")
    public String deleteItem(@PathVariable Long id,

                             RedirectAttributes redirectAttributes) {
        try {
            itemService.deleteItem(id);
            redirectAttributes.addFlashAttribute("message", "Item deleted successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/items";
    }

    @GetMapping("/restock/{id}")
    public String getRestockItem(@PathVariable Long id, Model model) {
        Item item = itemService.getItemById(id)
                .orElse(new Item());
        model.addAttribute("item", item);
        return "items-restock";
    }

    @PostMapping("/restock/{id}")
    public String restockItem(@PathVariable Long id,
                              @RequestParam int additionalQuantity,
                              RedirectAttributes redirectAttributes) {
        try {
            itemService.restockItem(id, additionalQuantity);
            redirectAttributes.addFlashAttribute("message", "Item restocked successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/items";
    }

    @GetMapping("/check-stock/{id}")
    public String checkStock(@PathVariable Long id,
                             @RequestParam int threshold,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            boolean isLow = itemService.isStockLow(id, threshold);
            model.addAttribute("isLow", isLow);
            model.addAttribute("itemId", id);
            model.addAttribute("threshold", threshold);
            return "/stock-status";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/items";
        }
    }
}
