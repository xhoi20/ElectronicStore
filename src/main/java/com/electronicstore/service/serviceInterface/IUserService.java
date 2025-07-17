package com.electronicstore.service.serviceInterface;

import com.electronicstore.dto.AuthRequest;
import com.electronicstore.dto.AuthResponse;
import com.electronicstore.dto.UserRegistrationRequest;
import com.electronicstore.dto.UserUpdateRequest;
import com.electronicstore.entity.User;
import com.electronicstore.entity.UserRole;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Set;
import java.util.Optional;

public interface IUserService {

    public ResponseEntity<Map<String, Object>> registerUser(
            UserRegistrationRequest request, Long sectorId, Set<Long> managedSectorIds);
    AuthResponse loginUser(AuthRequest authRequest);

    Optional<User> getUserById(Long id);

    Iterable<User> getAllUsers();

    void deleteUserById(Long id);

    public ResponseEntity<Map<String,Object>>updateUser(Long id, UserUpdateRequest updateRequest, Long sectorId, Set<Long>managedSectorIds, UserRole requestingUserRole);
}

