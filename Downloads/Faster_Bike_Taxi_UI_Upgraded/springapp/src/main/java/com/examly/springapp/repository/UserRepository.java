// package com.examly.springapp.repository;

// import java.util.Optional;
// import org.springframework.data.jpa.repository.JpaRepository;
// import com.examly.springapp.model.User;

// public interface UserRepository extends JpaRepository<User, Long> {

//   Optional<User> findByUsername(String username);

//   Optional<User> findByEmail(String email);

//   Optional<User> findByPhoneNumber(String phoneNumber);

//   boolean existsByEmail(String email);

//   boolean existsByUsername(String username);
// }
package com.examly.springapp.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.examly.springapp.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByPhoneNumber(String phoneNumber);
}