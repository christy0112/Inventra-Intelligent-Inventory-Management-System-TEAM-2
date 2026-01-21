package com.inventory.auth.repository;
import com.inventory.auth.model.User;


import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);
    User findByEmail(String email);
    boolean existsByRoleIgnoreCase(String role);
    User findFirstByRoleIgnoreCase(String role);
    User findByResetToken(String resetToken);
}
