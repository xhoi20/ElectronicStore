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

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("api/users1")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping
    public Iterable<User> getAllUser() {
        return userService.getAllUsers();
    }

@PostMapping("register")
public ResponseEntity<User> registerUser(
        @RequestBody UserRegistrationRequest request,
        @RequestParam(required = false) Long sectorId,
        @RequestParam(required = false) Set<Long> managedSectorIds) {
    try {
        UserRole role = UserRole.valueOf(request.getRole().toUpperCase());

        User createdUser = userService.registerUser(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                role,
                sectorId!= null ? sectorId : request.getSectorId(),
                managedSectorIds
        );
        return ResponseEntity.ok(createdUser);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(null);
    }
}


//    @PostMapping("login")
//    public ResponseEntity<User> loginUser(@RequestBody UserLoginRequest request) {
//        try {
//            User authenticatedUser = userService.loginUser(request.getEmail(), request.getPassword());
//            return ResponseEntity.ok(authenticatedUser);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        }
//    }


    @GetMapping("id")
    public ResponseEntity<User> getUserById(@RequestParam Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(
            @PathVariable Long id
         ) {
        try {
            userService.deleteUserById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

@PutMapping("/{id}")
public ResponseEntity<User> updateUser(
        @PathVariable Long id,
        @RequestBody UserUpdateRequest request,
        @RequestParam(required = false) Long sectorId,
        @RequestParam(required = false) Set<Long> managedSectorIds,
        @RequestParam UserRole requestingUserRole) {
    try {
        UserRole role = UserRole.valueOf(request.getRole().toUpperCase());

        User updatedUser = userService.updateUser(
                id,
                request.getName(),
                request.getEmail(),
                role,
                sectorId,
                managedSectorIds
              //  requestingUserRole
        );
        return ResponseEntity.ok(updatedUser);
    } catch (SecurityException e) {
        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    } catch (IllegalArgumentException e) {
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}

}