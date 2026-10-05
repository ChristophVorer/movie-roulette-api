package de.vorer.movieroulette.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindUserByUsername() {
        User user = createUser();

        userRepository.save(user);

        Optional<User> result = userRepository.findByEmail("christoph@example.com");

        assertTrue(result.isPresent());
        assertEquals("christoph@example.com", result.get().getEmail());
    }

    @Test
    void shouldReturnEmptyWhenUsernameDoesNotExist() {
        Optional<User> result = userRepository.findByEmail("unknown");

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnTrueWhenUsernameExists() {
        userRepository.save(createUser());

        assertTrue(userRepository.existsByUsername("christoph"));
    }

    @Test
    void shouldReturnFalseWhenUsernameDoesNotExist() {
        assertFalse(userRepository.existsByUsername("unknown"));
    }

    @Test
    void shouldReturnTrueWhenEmailExists() {
        userRepository.save(createUser());

        assertTrue(userRepository.existsByEmail("christoph@example.com"));
    }

    @Test
    void shouldReturnFalseWhenEmailDoesNotExist() {
        assertFalse(userRepository.existsByEmail("unknown@example.com"));
    }

    private User createUser() {
        User user = new User();
        user.setUsername("christoph");
        user.setEmail("christoph@example.com");
        user.setPasswordHash("hash");

        return user;
    }
}