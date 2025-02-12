package se.deved.blogg_app.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.filter.OncePerRequestFilter;
import se.deved.blogg_app.user.User;
import se.deved.blogg_app.user.UserRepository;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        Authentication potentialOAuth2Authentication = SecurityContextHolder.getContext().getAuthentication();
        // Vi kollar om man redan har autentiserat sig tidigare (med OAuth2)
        if (potentialOAuth2Authentication != null) {
            // Dubbelkolla så att det är OAuth2 autentisering och inget annat
            if (potentialOAuth2Authentication instanceof OAuth2AuthenticationToken oauth2Token) {
                // Hämta ut OAuth2 användaren och dess id
                OAuth2User oAuth2User = oauth2Token.getPrincipal();
                String oidcId = oAuth2User.getName();

                // Kolla om användaren (våran användare) finns i databas
                Optional<User> potentialUser = userRepository.findByOidcId(oidcId);
                if (potentialUser.isEmpty()) {
                    response.sendError(401, "Invalid token.");
                    return;
                }

                User user = potentialUser.get();

                // Koppla vår egen användare som Spring authentication istället för OAuth2 användaren
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                        user, user.getPassword(), user.getAuthorities()
                ));
                filterChain.doFilter(request, response);
                return;
            }
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        UUID userId;
        try {
            userId = jwtService.validateToken(authHeader);
        } catch (Exception exception) {
            response.sendError(401, "Invalid token.");
            return;
        }

        Optional<User> potentialUser = userRepository.findById(userId);
        if (potentialUser.isEmpty()) {
            response.sendError(401, "Invalid token.");
            return;
        }

        User user = potentialUser.get();

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                user, user.getPassword(), user.getAuthorities()
        ));
        filterChain.doFilter(request, response);
    }
}
