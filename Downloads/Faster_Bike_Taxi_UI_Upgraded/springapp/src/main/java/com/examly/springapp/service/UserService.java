// package com.examly.springapp.service;

// import java.util.List;
// import com.examly.springapp.model.User;

// public interface UserService {

//   User saveUser(User user);

//   List<User> getAllUsers();

//   User getUserById(Long id);

//   User updateUser(Long id, User user);

//   void deleteUser(Long id);
// }
package com.examly.springapp.service;

import java.util.List;

import com.examly.springapp.model.User;


public interface UserService {

    User saveUser(
        User user
    );


    List<User> getAllUsers();


    User getUserById(
        Long id
    );


    User updateUser(
        Long id,
        User user
    );


    void deleteUser(
        Long id
    );

}