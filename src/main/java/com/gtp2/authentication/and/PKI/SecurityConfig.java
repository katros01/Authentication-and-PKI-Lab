package com.gtp2.authentication.and.PKI;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/home").permitAll();
                    registry.requestMatchers("/home/admin").hasRole("ADMIN");
                    registry.requestMatchers("/home/user").hasRole("USER");
                    registry.anyRequest().authenticated();
                })
                .formLogin(formLogin -> formLogin.permitAll()
                        .successHandler(authenticationSuccessHandler()))
                .build();
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            String redirectUrl = request.getContextPath();
            if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                redirectUrl += "/home/admin";
            } else if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))) {
                redirectUrl += "/home/user";
            }
            response.sendRedirect(redirectUrl);
        };
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails adminUser = User.builder()
                .username("katros")
                .password(passwordEncoder().encode("123"))
                .roles("ADMIN")
                .build();

        UserDetails normalUser = User.builder()
                .username("martial")
                .password(passwordEncoder().encode("123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(normalUser, adminUser);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
