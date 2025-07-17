package com.electronicstore.repository;
import com.electronicstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);


    boolean existsByEmail(String email);


    boolean existsByName(String name);


    User findByName(String name);
}