package com.application.repository;

import com.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, CustomUserRepository {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
