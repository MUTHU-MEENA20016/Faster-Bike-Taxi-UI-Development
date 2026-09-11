// package com.examly.springapp.configuration;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// import com.examly.springapp.security.CustomUserDetailsService;
// import com.examly.springapp.security.JwtAuthenticationFilter;

// @Configuration
// public class SecurityConfig {

//     @Autowired
//     private CustomUserDetailsService userDetailsService;

//     @Autowired
//     private JwtAuthenticationFilter jwtAuthFilter;

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//         return config.getAuthenticationManager();
//     }

//     @Bean
//     public DaoAuthenticationProvider authenticationProvider() {
//         DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//         provider.setUserDetailsService(userDetailsService);
//         provider.setPasswordEncoder(passwordEncoder());
//         return provider;
//     }

//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         http
//             .csrf(csrf -> csrf.disable())
//             .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//             .authorizeHttpRequests(auth -> auth
//                 .antMatchers("/auth/**").permitAll()
//                 .antMatchers("/addBiketaxi", "/getAllBiketaxi").permitAll() // keeps existing test suite green
//                 .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
//                 .anyRequest().authenticated()
//             )
//             .authenticationProvider(authenticationProvider())
//             .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

//         return http.build();
//     }
// }
// package com.examly.springapp.configuration;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// import org.springframework.security.config.Customizer;
// import com.examly.springapp.security.CustomUserDetailsService;
// import com.examly.springapp.security.JwtAuthenticationFilter;

// @Configuration
// public class SecurityConfig {

//   @Autowired
//   private CustomUserDetailsService userDetailsService;

//   @Autowired
//   private JwtAuthenticationFilter jwtAuthFilter;

//   @Bean
//   public PasswordEncoder passwordEncoder() {
//     return new BCryptPasswordEncoder();
//   }

//   @Bean
//   public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//     return config.getAuthenticationManager();
//   }
//   @Bean
//   public DaoAuthenticationProvider authenticationProvider() {
//     DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//     provider.setUserDetailsService(userDetailsService);
//     provider.setPasswordEncoder(passwordEncoder());
//     return provider;
//   }

//   @Bean
//   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//     http
//       .cors(Customizer.withDefaults())
//       .csrf(csrf -> csrf.disable())
//       .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//       .authorizeHttpRequests(auth -> auth

//         // Public APIs
//         .antMatchers("/auth/**").permitAll()
//         .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
//         .antMatchers("/addBiketaxi", "/getAllBiketaxi").permitAll()

//         // ADMIN only
//         .antMatchers("/api/users/**").hasRole("ADMIN")
//         .antMatchers("/api/auditlogs/**").hasRole("ADMIN")

//         // MANAGER + ADMIN
//         .antMatchers("/api/background-checks/**")
//         .hasAnyRole("MANAGER", "ADMIN")

//         // DRIVER + MANAGER + ADMIN
//         .antMatchers("/api/applications/**")
//         .hasAnyRole("DRIVER", "MANAGER", "ADMIN")

//         .antMatchers("/api/documents/**")
//         .hasAnyRole("DRIVER", "MANAGER", "ADMIN")

//         .antMatchers("/api/vehicles/**")
//         .hasAnyRole("DRIVER", "MANAGER", "ADMIN")

//         .antMatchers("/api/performance/**")
//         .hasAnyRole("DRIVER", "MANAGER", "ADMIN")

//         .antMatchers("/api/notifications/**")
//         .hasAnyRole("DRIVER", "MANAGER", "ADMIN")

//         .anyRequest().authenticated())

//       .authenticationProvider(authenticationProvider())
//       .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

//     return http.build();
//   }
// }


// package com.examly.springapp.configuration;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.Customizer;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// import com.examly.springapp.security.CustomUserDetailsService;
// import com.examly.springapp.security.JwtAuthenticationFilter;

// @Configuration
// public class SecurityConfig {

//     @Autowired
//     private CustomUserDetailsService userDetailsService;

//     @Autowired
//     private JwtAuthenticationFilter jwtAuthFilter;

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration c) throws Exception {
//         return c.getAuthenticationManager();
//     }

//     @Bean
//     public DaoAuthenticationProvider authenticationProvider() {
//         DaoAuthenticationProvider p = new DaoAuthenticationProvider();
//         p.setUserDetailsService(userDetailsService);
//         p.setPasswordEncoder(passwordEncoder());
//         return p;
//     }

//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         http.cors(Customizer.withDefaults())
//             .csrf(csrf -> csrf.disable())
//             .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//             .authorizeHttpRequests(auth -> auth
//                 .antMatchers(
//                     "/auth/**",
//                     "/swagger-ui/**",
//                     "/v3/api-docs/**",
//                     "/addBiketaxi",
//                     "/getAllBiketaxi"
//                 ).permitAll()
//                 .antMatchers(
//                     "/api/users/**",
//                     "/api/auditlogs/**"
//                 ).hasRole("ADMIN")
//                 .antMatchers(
//                     "/api/applications/submit",
//                     "/api/applications/my",
//                     "/api/documents/temp",
//                     "/api/documents/application/**",
//                     "/api/documents/*/download"
//                 ).hasAnyRole("DRIVER", "MANAGER", "ADMIN")
//                 .antMatchers(
//                     "/api/applications/**",
//                     "/api/documents/**"
//                 ).hasAnyRole("MANAGER", "ADMIN")
//                 .antMatchers(
//                     "/api/background-checks/**"
//                 ).hasAnyRole("MANAGER", "ADMIN")
//                 .antMatchers(
//                     "/api/vehicles/**",
//                     "/api/notifications/**",
//                     "/api/performance/**"
//                 ).hasAnyRole("DRIVER", "MANAGER", "ADMIN")
//                 .anyRequest().authenticated()
//             )
//             .authenticationProvider(authenticationProvider())
//             .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

//         return http.build();
//     }
// }

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
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {

        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )
            .authorizeHttpRequests(auth -> auth

                /* PUBLIC */
                .antMatchers(
                    "/auth/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/addBiketaxi",
                    "/getAllBiketaxi"
                ).permitAll()

                /* DRIVER / MANAGER / ADMIN - specific endpoints */
                .antMatchers(
                    "/api/applications/submit",
                    "/api/applications/my",
                    "/api/documents/temp",
                    "/api/documents/application/**",
                    "/api/documents/*/download"
                ).hasAnyRole("DRIVER", "MANAGER", "ADMIN")

                /* ADMIN ONLY */
                .antMatchers(
                    "/api/users/**",
                    "/api/auditlogs/**"
                ).hasRole("ADMIN")

                /* MANAGER + ADMIN */
                .antMatchers(
                    "/api/applications/**",
                    "/api/documents/**",
                    "/api/background-checks/**"
                ).hasAnyRole("MANAGER", "ADMIN")

                /* AUTHENTICATED USERS */
                .antMatchers(
                    "/api/vehicles/**",
                    "/api/performance/**",
                    "/api/notifications/**"
                ).hasAnyRole("DRIVER", "MANAGER", "ADMIN")

                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(
                jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}