package com.example.Salon_Management_System.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        http
                // CORS enable
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Disable CSRF because using JWT
                .csrf(csrf -> csrf.disable())


                // URL permissions
                .authorizeHttpRequests(auth -> auth

                        // CORS preflight requests - always allow
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Test API
                        .requestMatchers("/V1/test/**").permitAll()
                        // User APIs
                        .requestMatchers(HttpMethod.POST, "/api/v1/user/save").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/user/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/user/all").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/user/search").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/user/update").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/user/delete").permitAll()
                        // Customer

                        .requestMatchers(HttpMethod.POST,"/api/v1/customers/save").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/customers/all").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/v1/customers/update").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/customers/delete/*").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/customers/search").permitAll()


                        // Other APIs require JWT
                        .anyRequest()
                        .authenticated()

                )
                // JWT no session
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                // Authentication provider
                .authenticationProvider(authenticationProvider())

                // JWT filter before username/password filter
                .addFilterBefore(
                        jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder(12);

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){


        CorsConfiguration configuration =
                new CorsConfiguration();


        configuration.setAllowedOriginPatterns(
                Arrays.asList(
                        "http://localhost:*",
                        "http://127.0.0.1:*",
                        "null"
                )
        );


        configuration.setAllowedMethods(
                Arrays.asList(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
        );


        configuration.setAllowedHeaders(
                Arrays.asList("*")
        );


        configuration.setExposedHeaders(
                Arrays.asList(
                        "Authorization",
                        "Content-Type"
                )
        );


        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();


        source.registerCorsConfiguration(
                "/**",
                configuration
        );


        return source;

    }

}