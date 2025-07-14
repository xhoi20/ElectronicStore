package com.electronicstore.controller.thymeleafcontroller;

import com.electronicstore.entity.User;
import com.electronicstore.entity.UserRole;
import com.electronicstore.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;

import java.util.Set;


@Controller
@RequestMapping("/users")
public class UserThymeleafController {

    @Autowired
    private UserService userService;


    @GetMapping
    public String getAllUsers(Model model) {
        Iterable<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user-list";
    }

@RequestMapping(value = "/user-form", method = {RequestMethod.GET, RequestMethod.POST})
public String handleUserForm(@ModelAttribute("user") @Valid User user,
                             BindingResult result,
                             @RequestParam(value = "sectorId", required = false) Long sectorId,
                             @RequestParam(value = "managedSectorIds", required = false) List<Long> managedSectorIds,
                             RedirectAttributes redirectAttributes,
                             Model model,
                             HttpServletRequest request) {

    model.addAttribute("roles", UserRole.values());
    model.addAttribute("sectors", userService.getAllSectors());


    if (request.getMethod().equalsIgnoreCase("GET")) {
        model.addAttribute("user", new User());
        return "user-form";
    }


    if (result.hasErrors()) {
        System.out.println("Validation errors: " + result.getAllErrors());
        return "user-form";
    }

    Set<Long> managedSectorIdsSet = managedSectorIds != null ? new HashSet<>(managedSectorIds) : new HashSet<>();
    try {
        userService.registerUser(user.getName(), user.getEmail(), user.getPassword(), user.getRole(), sectorId, managedSectorIdsSet);
        redirectAttributes.addFlashAttribute("successMessage", "User saved successfully!");
        return "redirect:/users";
    } catch (Exception e) {
        System.out.println("Error saving user: " + e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", "Failed to save user: " + e.getMessage());
        return "user-form";
    }
}



    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("Përdoruesi nuk u gjet"));
        model.addAttribute("user", user);
        model.addAttribute("roles", UserRole.values());

        return "edit-user";
    }

@PostMapping("/edit/{id}")
public String updateUser(@PathVariable Long id, @ModelAttribute("user") User user) {
    try {
        userService.updateUser(id, user.getName(), user.getEmail(), user.getRole(), null, new HashSet<>());
        return "redirect:/users";
    } catch (IllegalArgumentException e) {
        System.out.println("Error: " + e.getMessage());
        return "error"; // Create error.html
    } catch (Exception e) {
        System.out.println("Unexpected error: " + e.getMessage());
        return "error";
    }
}

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/users";
    }



}