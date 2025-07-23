package com.electronicstore.controller.thymeleafcontroller;

import com.electronicstore.dto.UserRegistrationRequest;
import com.electronicstore.dto.UserUpdateRequest;
import com.electronicstore.entity.User;
import com.electronicstore.entity.UserRole;
import com.electronicstore.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;

import java.util.Map;
import java.util.Set;


@Controller
@RequestMapping("/users")
public class UserThymeleafController {

    @Autowired
    private UserService userService;


    @GetMapping("/user-list")
    public String getAllUsers(Model model , Authentication authentication) {

        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            String role = authentication.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .findFirst()
                    .map(auth -> auth.replace("ROLE_", ""))
                    .orElse("");
            model.addAttribute("email", email);
            model.addAttribute("role", role);
            Iterable<User> users = userService.getAllUsers();
            model.addAttribute("users", users);
        }
        return "user-list";
    }

@RequestMapping(value = "/user-form", method = {RequestMethod.GET, RequestMethod.POST})
public String handleUserForm(@ModelAttribute("userRegistrationRequest") @Valid UserRegistrationRequest userRegistrationRequest,
                             BindingResult result,
                             @RequestParam(value = "sectorId", required = false) Long sectorId,
                             @RequestParam(value = "managedSectorIds", required = false) List<Long> managedSectorIds,
                             RedirectAttributes redirectAttributes,
                             Model model,
                             HttpServletRequest request) {



    if (request.getMethod().equalsIgnoreCase("GET")) {
        model.addAttribute("userRegistrationRequest", new UserRegistrationRequest());
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("sectors", userService.getAllSectors());
        return "user-form";
    }

    model.addAttribute("roles", UserRole.values());
    model.addAttribute("sectors", userService.getAllSectors());

    if (result.hasErrors()) {
        System.out.println("Validation errors: " + result.getAllErrors());
        return "user-form";
    }

    Set<Long> managedSectorIdsSet = managedSectorIds != null ? new HashSet<>(managedSectorIds) : new HashSet<>();

        ResponseEntity<Map<String, Object>> response =  userService.registerUser(userRegistrationRequest,  sectorId, managedSectorIdsSet);
    if (response.getStatusCode().is2xxSuccessful()) {
        redirectAttributes.addFlashAttribute("successMessage", "User saved successfully!");
        return "redirect:/users/user-list";
    } else {

        model.addAttribute("errorMessage", response.getBody().get("message"));
        return "user-form";
    }

}

    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("Perdoruesi nuk u gjet"));
        model.addAttribute("user", user);
        model.addAttribute("roles", UserRole.values());

        return "edit-user";
    }



    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id,
                             @Valid @ModelAttribute("user") UserUpdateRequest updateRequest,
                             BindingResult result,
                             @RequestParam(value = "sectorId", required = false) Long sectorId,
                             @RequestParam(value = "managedSectorIds", required = false) List<Long> managedSectorIds,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (result.hasErrors()) {
            System.out.println("Validation errors: " + result.getAllErrors());
            model.addAttribute("roles", UserRole.values());
            model.addAttribute("sectors", userService.getAllSectors());
            return "edit-user";
        }

        Set<Long> managedSectorIdsSet = managedSectorIds != null ? new HashSet<>(managedSectorIds) : new HashSet<>();
        try {
            ResponseEntity<Map<String, Object>> response = userService.updateUser(id, updateRequest, sectorId, managedSectorIdsSet, null); // null for requestingUserRole if not used
            if (response.getStatusCode() == HttpStatus.OK) {
                redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
                return "redirect:/users/user-list";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", response.getBody().get("message"));
                return "redirect:/users/edit/" + id;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update user: " + e.getMessage());
            return "redirect:/users/edit/" + id;
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Unexpected error occurred");
            return "redirect:/users/edit/" + id;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/users/user-list";
    }



}