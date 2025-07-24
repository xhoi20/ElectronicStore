package com.electronicstore.repository;
import com.electronicstore.entity.User;
import com.electronicstore.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findByRole(UserRole role);

    boolean existsByEmail(String email);


    boolean existsByName(String name);


    User findByName(String name);
}