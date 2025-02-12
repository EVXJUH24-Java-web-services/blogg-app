package se.deved.blogg_app.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.deved.blogg_app.security.JWTService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

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

        User user = new User(username, passwordEncoder.encode(password));

        // INSERT INTO user () VALUES ();
        return userRepository.save(user);
    }

    // Skapa och koppla en OpenID-connect användare med vår egen model (User)
    public User createOpenIdUser(String username, String oidcId) {
        User user = new User(username, null);
        user.setOidcId(oidcId);
        user.setOidcProvider("github"); // Vi använder bara GitHub så vi kan hårdkoda denna.

        // INSERT INTO user () VALUES ();
        return userRepository.save(user);
    }

    public String login(String username, String password) {
        User user = userRepository.findByName(username).orElseThrow();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Wrong username or password.");
        }

        return jwtService.generateToken(user.getId());
    }

    public Optional<User> findByOpenId(String id) {
        return userRepository.findByOidcId(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
