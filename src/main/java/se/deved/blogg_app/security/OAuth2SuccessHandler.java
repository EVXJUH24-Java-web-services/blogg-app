package se.deved.blogg_app.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import se.deved.blogg_app.user.User;
import se.deved.blogg_app.user.UserService;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        // Hämta ut token/autentiserings objektet från requesten
        // Vi castar till en OAuth2Token för att vi vill komma åt OAuth2 användaren
        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
        // Hämta ut OAuth2 användaren som kommer med token inputten
        OAuth2User oAuth2User = oauth2Token.getPrincipal();

        System.out.println(oauth2Token.getAuthorizedClientRegistrationId());

        // Hämta ut id:t på OAuth2 användaren (det är ett id även om det kallas name)
        String oidcId = oAuth2User.getName();
        // Hämta ut användarnamnet på OAuth2 användaren (det riktiga namnet), alltså GitHub namnet
        String username = oAuth2User.getAttribute("login");

        // Kolla om vi har en egen användare i databasen som redan är kopplad
        Optional<User> existingUser = userService.findByOpenId(oidcId);
        // Om vi inte har en egen använda skapar vi en ny, med koppling
        if (existingUser.isEmpty()) {
            User user = userService.createOpenIdUser(username, oidcId);
            System.out.println("Registered user '" + user.getName() + "' through OpenID-connect!");
        }
    }
}
