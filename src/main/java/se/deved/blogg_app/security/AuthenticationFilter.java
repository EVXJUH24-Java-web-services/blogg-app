package se.deved.blogg_app.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import se.deved.blogg_app.user.User;
import se.deved.blogg_app.user.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
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
