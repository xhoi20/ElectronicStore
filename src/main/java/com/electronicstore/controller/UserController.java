package com.electronicstore.controller;

import com.electronicstore.dto.UserRegistrationRequest;
import com.electronicstore.dto.UserUpdateRequest;
import com.electronicstore.entity.User;
import com.electronicstore.entity.UserRole;
import com.electronicstore.service.serviceInterface.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("api/users1")
public class UserController extends BaseController{
    @Autowired
    private IUserService userService;

    @GetMapping
    public Iterable<User> getAllUser() {
        return userService.getAllUsers();
    }


@PostMapping("register")
public ResponseEntity<Map<String, Object>> registerUser(
        @RequestBody UserRegistrationRequest request,
        @RequestParam(required = false) Long sectorId,
        @RequestParam(required = false) Set<Long> managedSectorIds) {
    checkManagerRole();
    return userService.registerUser(request, sectorId, managedSectorIds);
}

    @GetMapping("id")
    public ResponseEntity<User> getUserById(@RequestParam Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUserById(@PathVariable Long id) {
        try {
            checkManagerRole();
            userService.deleteUserById(id);
            return createSuccessResponse(null, "User deleted successfully", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return handleException(e);
        }
    }


   @PutMapping("{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest updateRequest,
            @RequestParam(required = false) Long sectorId,
            @RequestParam(required = false) Set<Long> managedSectorIds,
            @RequestParam(required = false) UserRole requestingUserRole) {
       checkManagerRole();
       return userService.updateUser(id, updateRequest, sectorId, managedSectorIds, requestingUserRole);
    }
}