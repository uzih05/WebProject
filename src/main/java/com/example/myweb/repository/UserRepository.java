package com.example.myweb.repository;

import com.example.myweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email); // 추가
    Optional<User> findByResetToken(String resetToken); // 추가
}