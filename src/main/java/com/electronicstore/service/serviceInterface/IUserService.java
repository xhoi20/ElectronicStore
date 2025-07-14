package com.electronicstore.service.serviceInterface;

import com.electronicstore.dto.AuthRequest;
import com.electronicstore.dto.AuthResponse;
import com.electronicstore.entity.User;
import com.electronicstore.entity.UserRole;

import java.util.Set;
import java.util.Optional;

public interface IUserService {
    User registerUser(String emri, String email, String rawPassword, UserRole role, Long sectorId, Set<Long> managedSectorIds);

    AuthResponse loginUser(AuthRequest authRequest);

    Optional<User> getUserById(Long id);

    Iterable<User> getAllUsers();

    void deleteUserById(Long id);

    User updateUser(Long id, String name, String email,
                    UserRole role, Long sectorId, Set<Long> managedSectorIds);
}

