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

                        .requestMatchers(HttpMethod.PUT, "/api/v1/user/change-password").authenticated()
                        // Admin User Management APIs
                        .requestMatchers("/api/v1/admin-users/**").permitAll()

                        // Customer

                        .requestMatchers(HttpMethod.POST,"/api/v1/customers/save").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/customers/all").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/v1/customers/update").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/customers/delete/*").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/customers/search").permitAll()


                        // STAFF APIs
                        .requestMatchers(HttpMethod.GET, "/api/v1/staff/all").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/staff/search").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/staff/*").permitAll()

                        .requestMatchers("/api/v1/staff/**").authenticated()

                        // SERVICE APIs

                        .requestMatchers(HttpMethod.POST, "/api/v1/services/save").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/services/all").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/services/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/services/search").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/services/update").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/services/delete/*").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/services/status/*").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/services/assign-staff/*").permitAll()

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Appointment
                        .requestMatchers(HttpMethod.POST, "/api/v1/appointment/save").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/appointment/all").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/appointment/date/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/appointment/*").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/appointment/update/*").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/appointment/delete/*").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/appointment/status/*").permitAll()

                        //Payment
                        .requestMatchers(HttpMethod.POST, "/api/v1/payments/save").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/payments/all").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/payments/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/payments/date-range").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/payments/update/*").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/payments/delete/*").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/payments/refund/*").permitAll()


                        // Frontend static resources
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/static/**"
                        ).permitAll()

                        // Settings API
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/settings"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/settings"
                        ).permitAll()

                        .requestMatchers(
                                "/api/settings/**"
                        ).permitAll()

                        //Feedback
                        .requestMatchers("/api/v1/feedback/**").permitAll()

                        .requestMatchers("/api/v1/reports/**").permitAll()

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
                        "PATCH",
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