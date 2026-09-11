// // package com.examly.springapp.controller;

// // import org.springframework.beans.factory.annotation.Autowired;
// // import org.springframework.http.HttpStatus;
// // import org.springframework.http.ResponseEntity;
// // import org.springframework.security.authentication.AuthenticationManager;
// // import org.springframework.security.authentication.BadCredentialsException;
// // import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// // import org.springframework.security.core.userdetails.UserDetails;
// // import org.springframework.security.crypto.password.PasswordEncoder;
// // import org.springframework.web.bind.annotation.PostMapping;
// // import org.springframework.web.bind.annotation.RequestBody;
// // import org.springframework.web.bind.annotation.RequestMapping;
// // import org.springframework.web.bind.annotation.RestController;

// // import com.examly.springapp.dto.AuthRequest;
// // import com.examly.springapp.dto.AuthResponse;
// // // import com.examly.springapp.model.Role;
// // import com.examly.springapp.model.User;
// // import com.examly.springapp.model.User.Role;
// // import com.examly.springapp.repository.UserRepository;
// // import com.examly.springapp.security.CustomUserDetailsService;
// // import com.examly.springapp.security.JwtUtil;

// // @RestController
// // @RequestMapping("/auth")
// // public class AuthController {

// //     @Autowired
// //     private AuthenticationManager authenticationManager;

// //     @Autowired
// //     private CustomUserDetailsService userDetailsService;

// //     @Autowired
// //     private JwtUtil jwtUtil;

// //     @Autowired
// //     private UserRepository userRepository;

// //     @Autowired
// //     private PasswordEncoder passwordEncoder;

// //     @PostMapping("/register")
// //     public ResponseEntity<?> register(@RequestBody User request) {
// //         if (userRepository.existsByUsername(request.getUsername())) {
// //             return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
// //         }
// //         if (userRepository.existsByEmail(request.getEmail())) {
// //             return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
// //         }

// //         User user = new User();
// //         user.setUsername(request.getUsername());
// //         user.setEmail(request.getEmail());
// //         user.setPasswordHash(passwordEncoder.encode(request.getPasswordHash()));
// //         user.setPhoneNumber(request.getPhoneNumber());
// //         user.setRole(request.getRole() != null ? request.getRole() : Role.DRIVER);

// //         userRepository.save(user);
// //         return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
// //     }

// //     @PostMapping("/login")
// //     public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
// //         try {
// //             authenticationManager.authenticate(
// //                     new UsernamePasswordAuthenticationToken(
// //                             authRequest.getUsername(), authRequest.getPassword()));
// //         } catch (BadCredentialsException e) {
// //             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
// //         }

// //         UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
// //         User user = userRepository.findByUsername(authRequest.getUsername()).get();

// //         String token = jwtUtil.generateToken(userDetails, user.getRole().name());
// //         return ResponseEntity.ok(new AuthResponse(token));
// //     }
// // }
// package com.examly.springapp.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.examly.springapp.dto.AuthRequest;
// import com.examly.springapp.dto.AuthResponse;
// // import com.examly.springapp.model.Role;
// import com.examly.springapp.model.User;
// import com.examly.springapp.model.User.Role;
// import com.examly.springapp.repository.UserRepository;
// import com.examly.springapp.security.CustomUserDetailsService;
// import com.examly.springapp.security.JwtUtil;

// @RestController
// @RequestMapping("/auth")
// public class AuthController {

//     @Autowired
//     private AuthenticationManager authenticationManager;

//     @Autowired
//     private CustomUserDetailsService userDetailsService;

//     @Autowired
//     private JwtUtil jwtUtil;

//     @Autowired
//     private UserRepository userRepository;

//     @Autowired
//     private PasswordEncoder passwordEncoder;

//     @PostMapping("/register")
//     public ResponseEntity<?> register(@RequestBody User request) {
//         if (userRepository.existsByUsername(request.getUsername())) {
//             return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
//         }
//         if (userRepository.existsByEmail(request.getEmail())) {
//             return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
//         }

//         User user = new User();
//         user.setUsername(request.getUsername());
//         user.setEmail(request.getEmail());
//         user.setPasswordHash(passwordEncoder.encode(request.getPasswordHash()));
//         user.setPhoneNumber(request.getPhoneNumber());
//         user.setRole(request.getRole() != null ? request.getRole() : Role.DRIVER);

//         userRepository.save(user);
//         return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
//     }

//     @PostMapping("/login")
//     public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
//         try {
//             authenticationManager.authenticate(
//                     new UsernamePasswordAuthenticationToken(
//                             authRequest.getUsername(), authRequest.getPassword()));
//         } catch (BadCredentialsException e) {
//             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
//         }

//         UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
//         User user = userRepository.findByUsername(authRequest.getUsername()).get();

//         String token = jwtUtil.generateToken(userDetails, user.getRole().name());
//         return ResponseEntity.ok(new AuthResponse(token));
//     }
// }
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
    public ResponseEntity<?> register(@RequestBody Map<String, Object> request) {

        String username = request.get("username") != null
                ? request.get("username").toString()
                : null;

        String email = request.get("email") != null
                ? request.get("email").toString()
                : null;

        String password = request.get("passwordHash") != null
                ? request.get("passwordHash").toString()
                : request.get("password") != null
                    ? request.get("password").toString()
                    : null;

        String phoneNumber = request.get("phoneNumber") != null
                ? request.get("phoneNumber").toString()
                : null;

        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Username is required");
        }

        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        if (password == null || password.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Password is required");
        }

        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Phone number is required");
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

        User user = new User();

        user.setUsername(username.trim());
        user.setEmail(email.trim());

        // Convert the plain password into a BCrypt password before saving.
        user.setPasswordHash(passwordEncoder.encode(password));

        user.setPhoneNumber(phoneNumber.trim());
        user.setRole(Role.DRIVER);

        userRepository.save(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {

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

        return ResponseEntity.ok(new AuthResponse(token));
    }
}