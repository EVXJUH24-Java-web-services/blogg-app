package se.deved.blogg_app.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SecuritySecrets {

    @Value("${jwt-secret}")
    private String jwtSecret;
}
