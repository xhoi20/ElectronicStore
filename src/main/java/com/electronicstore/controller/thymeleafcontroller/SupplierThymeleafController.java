package com.electronicstore.controller.thymeleafcontroller;

import com.electronicstore.entity.Supplier;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import com.electronicstore.service.serviceInterface.ISupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/suppliers")
public class SupplierThymeleafController {
    @Autowired
    private ISupplierService supplierService;
    @GetMapping
    public String getAllSuppliers(Model model) {

        Iterable<Supplier> suppliers =supplierService.getAllSuppliers();
        model.addAttribute("suppliers", suppliers);
        return "supplier-list";
    }
  @GetMapping("/add")
public String addSupplier(Model model) {
        model.addAttribute("supplier", new Supplier());
        return "supplier-form";
  }

@PostMapping("/save")
public String saveSupplier(@Valid @ModelAttribute("supplier") Supplier supplier,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
        return "supplier-form";
    }
    try {

        Map<String, String> supplierData = new HashMap<>();
        supplierData.put("name", supplier.getName());
        supplierData.put("email", supplier.getEmail());
        supplierData.put("adresa", supplier.getAdresa());


            supplierService.addSupplier(supplierData);
            redirectAttributes.addFlashAttribute("message", "Supplier added successfully");

    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        return "redirect:/suppliers";
    }
    return "redirect:/suppliers";
}

  @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
      Supplier supplier = supplierService.getSupplierById(id)
              .orElseThrow(() -> new RuntimeException("Furnitori nuk u gjet nuk u gjet"));
      model.addAttribute("supplier", supplier);

      return "edit-supplier";
  }

  @PostMapping("/edit/{id}")
  public String updateSupplier(@PathVariable Long id, @ModelAttribute("supplier") Supplier supplier) {
      try {
          Map<String, String> supplierData = new HashMap<>();
          supplierData.put("name", supplier.getName());
          supplierData.put("email", supplier.getEmail());
          supplierData.put("adresa", supplier.getAdresa());

          supplierService.updateSupplier(id,supplierData);
          return "redirect:/suppliers";
      } catch (IllegalArgumentException e) {
          System.out.println("Error: " + e.getMessage());
          return "error";
      } catch (Exception e) {
          System.out.println("Unexpected error: " + e.getMessage());
          return "error";
      }
  }
    @GetMapping("/delete/{id}")
    public String deleteSupplier(@PathVariable("id") long id) {
        supplierService.deleteSupplier(id);
        return "redirect:/suppliers";
    }
}
