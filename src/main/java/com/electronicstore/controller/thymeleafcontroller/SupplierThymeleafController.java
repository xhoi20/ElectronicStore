package com.electronicstore.controller.thymeleafcontroller;

import com.electronicstore.entity.Supplier;
import com.electronicstore.entity.User;
import com.electronicstore.entity.UserRole;
import org.springframework.ui.Model;
import com.electronicstore.service.SupplierService;
import com.electronicstore.service.serviceInterface.ISupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

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
    public String saveSupplier(@ModelAttribute("supplier") Supplier supplier) {
        if(supplier.getId()==null){
            supplierService.addSupplier(supplier);
        }else{
            supplierService.updateSupplier(supplier);
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
          supplierService.updateSupplier(supplier);
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
