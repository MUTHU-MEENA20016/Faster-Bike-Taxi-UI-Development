package com.examly.springapp.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.examly.springapp.security.CustomUserDetailsService;
import com.examly.springapp.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;


    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration
                .getAuthenticationManager();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();

        provider.setUserDetailsService(
                userDetailsService
        );

        provider.setPasswordEncoder(
                passwordEncoder()
        );

        return provider;
    }


    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http)
            throws Exception {

        http

            .cors(
                Customizer.withDefaults()
            )

            .csrf(
                csrf -> csrf.disable()
            )

            .sessionManagement(
                session ->
                    session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS
                    )
            )

            .authorizeHttpRequests(
                auth -> auth


                    /*
                     * PUBLIC
                     */
                    .antMatchers(
                        "/auth/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/addBiketaxi",
                        "/getAllBiketaxi"
                    )
                    .permitAll()


                    /*
                     * DRIVER + MANAGER + ADMIN
                     */
                    .antMatchers(
                        "/api/applications/submit",
                        "/api/applications/my",
                        "/api/documents/temp",
                        "/api/documents/application/**",
                        "/api/documents/*/download"
                    )
                    .hasAnyRole(
                        "DRIVER",
                        "MANAGER",
                        "ADMIN"
                    )


                    /*
                     * DRIVER ONLY
                     *
                     * A driver can replace ONLY
                     * a rejected document belonging
                     * to their own application.
                     *
                     * IMPORTANT:
                     * This must come BEFORE
                     * /api/documents/**
                     */
                    .antMatchers(
                        "/api/documents/*/replace"
                    )
                    .hasRole(
                        "DRIVER"
                    )


                    /*
                     * ADMIN ONLY
                     */
                    .antMatchers(
                        "/api/users/**",
                        "/api/auditlogs/**"
                    )
                    .hasRole(
                        "ADMIN"
                    )


                    /*
                     * MANAGER + ADMIN
                     *
                     * This comes AFTER the DRIVER
                     * replacement endpoint.
                     */
                    .antMatchers(
                        "/api/applications/**",
                        "/api/documents/**",
                        "/api/background-checks/**"
                    )
                    .hasAnyRole(
                        "MANAGER",
                        "ADMIN"
                    )


                    /*
                     * AUTHENTICATED USERS
                     */
                    .antMatchers(
                        "/api/vehicles/**",
                        "/api/performance/**",
                        "/api/notifications/**"
                    )
                    .hasAnyRole(
                        "DRIVER",
                        "MANAGER",
                        "ADMIN"
                    )


                    .anyRequest()
                    .authenticated()
            )

            .authenticationProvider(
                authenticationProvider()
            )

            .addFilterBefore(
                jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class
            );


        return http.build();
    }
}