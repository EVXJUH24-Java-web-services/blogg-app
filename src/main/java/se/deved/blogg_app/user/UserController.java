package se.deved.blogg_app.user;

import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.deved.blogg_app.utility.ErrorResponseDTO;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserDTO createUser) {
        try {
            User user = userService.createUser(createUser.username, createUser.password);
            return ResponseEntity.ok(new UserResponseDTO(user.getId(), user.getName()));
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(new ErrorResponseDTO(exception.getMessage()));
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class CreateUserDTO {
        public String username;
        public String password;
    }

    @RequiredArgsConstructor
    @Getter
    private static class UserResponseDTO {
        private final UUID id;
        private final String username;
    }
}
