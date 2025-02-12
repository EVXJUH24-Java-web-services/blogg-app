package se.deved.blogg_app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.deved.blogg_app.user.User;
import se.deved.blogg_app.user.UserRepository;
import se.deved.blogg_app.user.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class UserServiceUnitTests {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void testCreateUser() {
        // Given
        var userRepository = Mockito.mock(UserRepository.class);
        Mockito.when(userRepository.save(Mockito.any(User.class))).then(invoke -> invoke.getArguments()[0]);

        var userService = new UserService(userRepository, passwordEncoder, null);
        String username = "Ironman";
        String password = "tonystark";

        // When
        User user = userService.createUser(username, password);

        // Then
        Assertions.assertNotNull(user);
        Assertions.assertEquals(username, user.getName());
        Assertions.assertNotNull(user.getId());
        Assertions.assertTrue(user.getComments().isEmpty());
        Mockito.verify(userRepository).save(user);
    }

    @Test
    void testCreateUserWithShortName() {
        // Given
        var userRepository = Mockito.mock(UserRepository.class);
        Mockito.when(userRepository.save(Mockito.any(User.class))).then(invoke -> invoke.getArguments()[0]);
ö
        var userService = new UserService(userRepository, passwordEncoder, null);
        String username = "Iron";
        String password = "tonystark";

        // When + Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.createUser(username, password));
    }
}
