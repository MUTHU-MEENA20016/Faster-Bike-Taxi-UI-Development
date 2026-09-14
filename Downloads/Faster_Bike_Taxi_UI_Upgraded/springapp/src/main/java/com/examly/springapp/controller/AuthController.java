package com.examly.springapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.examly.springapp.dto.AuthRequest;
import com.examly.springapp.dto.AuthResponse;
import com.examly.springapp.model.User;
import com.examly.springapp.model.User.Role;
import com.examly.springapp.repository.UserRepository;
import com.examly.springapp.security.CustomUserDetailsService;
import com.examly.springapp.security.JwtUtil;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody Map<String, Object> request) {

        String username = request.get("username") != null
                ? request.get("username").toString().trim()
                : null;

        String email = request.get("email") != null
                ? request.get("email").toString().trim()
                : null;

        String password = request.get("passwordHash") != null
                ? request.get("passwordHash").toString()
                : request.get("password") != null
                    ? request.get("password").toString()
                    : null;

        String phoneNumber = request.get("phoneNumber") != null
                ? request.get("phoneNumber").toString().trim()
                : null;

        if (username == null || username.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Username is required");
        }

        if (email == null || email.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Email is required");
        }

        if (password == null || password.trim().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Password is required");
        }

        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Phone number is required");
        }

        if (userRepository.existsByUsername(username)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        }

        if (userRepository.existsByEmail(email)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Email already exists");
        }

        /*
         * Each registered user must have a unique phone number.
         * This prevents another account from being created with
         * the same mobile number.
         */
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Phone number already registered");
        }

        User user = new User();

        user.setUsername(username);
        user.setEmail(email);

        // Store the password as a BCrypt hash.
        user.setPasswordHash(
                passwordEncoder.encode(password)
        );

        user.setPhoneNumber(phoneNumber);

        // Public registration creates DRIVER accounts.
        user.setRole(Role.DRIVER);

        userRepository.save(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody AuthRequest authRequest) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        authRequest.getUsername()
                );

        User user = userRepository
                .findByUsername(authRequest.getUsername())
                .get();

        String token = jwtUtil.generateToken(
                userDetails,
                user.getRole().name()
        );

        return ResponseEntity.ok(
                new AuthResponse(token)
        );
    }
}