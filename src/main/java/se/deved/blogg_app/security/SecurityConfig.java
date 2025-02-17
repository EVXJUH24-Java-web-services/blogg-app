package se.deved.blogg_app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import se.deved.blogg_app.user.UserRepository;
import se.deved.blogg_app.user.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JWTService jwtService,
            UserRepository userRepository,
            UserService userService,
            OAuth2SuccessHandler oAuth2SuccessHandler
    ) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .userDetailsService(userService)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/blog-post").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/blog-post/like/**", "/blog-post/dislike/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/comment").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/admin/delete-post").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth -> {
                    oauth.successHandler(oAuth2SuccessHandler);
                })
                .addFilterAfter(new AuthenticationFilter(jwtService, userRepository), OAuth2LoginAuthenticationFilter.class);

        return http.build();
    }

    // DELETE_ALL_POSTS
    // ADMIN

    // UserDetails
    // UserDetailsService
    // Authority
    // Role

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
