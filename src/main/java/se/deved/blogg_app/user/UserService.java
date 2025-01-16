package se.deved.blogg_app.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username may not be null or empty");
        }

        if (username.length() < 5) {
            throw new IllegalArgumentException("Username must be at least 5 characters.");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password may not be null or empty");
        }

        if (password.length() < 5) {
            throw new IllegalArgumentException("Password must be at least 5 characters.");
        }

        User user = new User(username, password);

        // INSERT INTO user () VALUES ();
        return userRepository.save(user);
    }
}
