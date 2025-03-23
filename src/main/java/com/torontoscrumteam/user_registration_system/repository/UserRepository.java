package com.torontoscrumteam.user_registration_system.repository;

import com.torontoscrumteam.user_registration_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    default Optional<User> findByUsernameOrEmail(String username, String email) {
        Optional<User> userByUsername = findByUsername(username);
        return userByUsername.isPresent() ? userByUsername : findByEmail(email);
    }
}