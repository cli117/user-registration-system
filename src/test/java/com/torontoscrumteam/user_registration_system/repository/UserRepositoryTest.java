package com.torontoscrumteam.user_registration_system.repository;

import com.torontoscrumteam.user_registration_system.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager; // 用于更精细的控制，例如刷新缓存

    @Test
    public void testSaveUser() {
        User user = new User("testuser2", "password2", "test2@example.com");
        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);

        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser2");
    }

    @Test
    public void testFindByUsername() {
        User user = new User("testuser3", "password3", "test3@example.com");
        entityManager.persist(user);
        entityManager.flush();

        Optional<User> foundUser = userRepository.findByUsername("testuser3");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test3@example.com");
    }

    @Test
    public void testFindByEmail() {
        User user = new User("testuser3", "password3", "test3@example.com");
        entityManager.persist(user);
        entityManager.flush();

        Optional<User> foundUser = userRepository.findByEmail("test3@example.com");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser3");
    }
}