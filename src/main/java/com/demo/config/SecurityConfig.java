package com.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.security.enabled:true}")
    private boolean securityEnabled;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**", "/h2-console/**"))
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin()));

        if (securityEnabled) {
            http
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/kitchen.html", "/admin.html").hasRole("STAFF")
                    .requestMatchers(HttpMethod.GET, "/api/orders").hasRole("STAFF")
                    .requestMatchers(HttpMethod.PUT, "/api/orders/**").hasRole("STAFF")
                    .requestMatchers("/api/menu/all").hasRole("STAFF")
                    .requestMatchers(HttpMethod.POST, "/api/menu").hasRole("STAFF")
                    .requestMatchers(HttpMethod.PUT, "/api/menu/**").hasRole("STAFF")
                    .requestMatchers(HttpMethod.DELETE, "/api/menu/**").hasRole("STAFF")
                    .anyRequest().permitAll())
                .formLogin(withDefaults());
        } else {
            http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        }

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        var admin = User.builder()
                .username(adminUsername)
                .password(encoder.encode(adminPassword))
                .roles("STAFF")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }
}
