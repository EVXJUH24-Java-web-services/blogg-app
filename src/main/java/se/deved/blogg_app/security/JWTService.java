package se.deved.blogg_app.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class JWTService {

    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    @Autowired
    public JWTService(SecuritySecrets securitySecrets) {
        this.algorithm = Algorithm.HMAC256(securitySecrets.getJwtSecret());
        this.verifier = JWT.require(algorithm).withIssuer("auth0").build();
    }

    public String generateToken(UUID userId) {
        return JWT.create()
                .withIssuer("auth0")
                .withSubject(userId.toString())
                .withExpiresAt(Instant.now().plus(30, ChronoUnit.MINUTES))
                .sign(algorithm);
    }

    public UUID validateToken(String token) {
        DecodedJWT jwt = verifier.verify(token);
        return UUID.fromString(jwt.getSubject());
    }
}
