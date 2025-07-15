package com.electronicstore.controller;

import com.electronicstore.dto.UserLoginRequest;
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
    try {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return createErrorResponse("Name is missing or empty", HttpStatus.BAD_REQUEST);
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return createErrorResponse("Email is missing or empty", HttpStatus.BAD_REQUEST);
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return createErrorResponse("Password is missing or empty", HttpStatus.BAD_REQUEST);
        }
        if (request.getRole() == null) {
            return createErrorResponse("Role cannot be null", HttpStatus.BAD_REQUEST);
        }

        UserRole role = UserRole.valueOf(request.getRole().toUpperCase());
        checkManagerRole(); // Vetëm manager mund të krijojë përdorues të rinj
        User createdUser = userService.registerUser(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                role,
                sectorId!= null ? sectorId : request.getSectorId(),
                managedSectorIds
        );
        return createSuccessResponse(createdUser, "User registered successfully", HttpStatus.CREATED);
    } catch (Exception e) {
        return handleException(e);
    }
}




    @GetMapping("id")
    public ResponseEntity<User> getUserById(@RequestParam Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUserById(
            @PathVariable Long id
         ) {
        try {
            checkManagerRole();
            userService.deleteUserById(id);
            return createSuccessResponse(null, "User deleted successfully", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return handleException(e);
        }
    }


   @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest request,
            @RequestParam(required = false) Long sectorId,
            @RequestParam(required = false) Set<Long> managedSectorIds,
            @RequestParam(required = false) UserRole requestingUserRole) {
        try {
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                return createErrorResponse("Name is missing or empty", HttpStatus.BAD_REQUEST);
            }
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return createErrorResponse("Email is missing or empty", HttpStatus.BAD_REQUEST);
            }
            if (request.getRole() == null) {
                return createErrorResponse("Role cannot be null", HttpStatus.BAD_REQUEST);
            }

            UserRole role = UserRole.valueOf(request.getRole().toUpperCase());
            checkManagerRole();
            User updatedUser = userService.updateUser(
                    id,
                    request.getName(),
                    request.getEmail(),
                    role,
                    sectorId,
                    managedSectorIds
            );
            return createSuccessResponse(updatedUser, "User updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }
}