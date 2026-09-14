// // package com.examly.springapp.controller;

// // import java.util.List;

// // import org.springframework.beans.factory.annotation.Autowired;
// // import org.springframework.web.bind.annotation.*;

// // import com.examly.springapp.model.User;
// // import com.examly.springapp.service.UserService;

// // @RestController
// // @RequestMapping("/api/users")
// // @CrossOrigin("*")
// // public class UserController {

// //   @Autowired
// //   private UserService userService;

// //   @PostMapping
// //   public User saveUser(@RequestBody User user){
// //     return userService.saveUser(user);
// //   }

// //   @GetMapping
// //   public List<User> getAllUsers(){
// //     return userService.getAllUsers();
// //   }

// //   @GetMapping("/{id}")
// //   public User getUserById(@PathVariable Long id){
// //     return userService.getUserById(id);
// //   }

// //   @PutMapping("/{id}")
// //   public User updateUser(@PathVariable Long id,
// //               @RequestBody User user){
// //     return userService.updateUser(id,user);
// //   }

// //   @DeleteMapping("/{id}")
// //   public void deleteUser(@PathVariable Long id){
// //     userService.deleteUser(id);
// //   }
// // }
// package com.examly.springapp.controller;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;

// import com.examly.springapp.model.User;
// import com.examly.springapp.service.UserService;

// @RestController
// @RequestMapping("/api/users")
// @CrossOrigin("*")
// public class UserController {

//     @Autowired
//     private UserService userService;

//     @PostMapping
//     public User saveUser(@RequestBody User user) {
//         return userService.saveUser(user);
//     }

//     @GetMapping
//     public List<User> getAllUsers() {
//         return userService.getAllUsers();
//     }

//     @GetMapping("/{id}")
//     public User getUserById(@PathVariable Long id) {
//         return userService.getUserById(id);
//     }

//     @PutMapping("/{id}")
//     public User updateUser(
//             @PathVariable Long id,
//             @RequestBody User user) {

//         return userService.updateUser(id, user);
//     }

//     @DeleteMapping("/{id}")
//     public void deleteUser(@PathVariable Long id) {
//         userService.deleteUser(id);
//     }
// }
package com.examly.springapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.examly.springapp.model.User;
import com.examly.springapp.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping
    public User saveUser(
            @RequestBody User user) {

        return userService.saveUser(user);
    }


    @GetMapping
    public List<User> getAllUsers() {

        return userService.getAllUsers();
    }


    @GetMapping("/{id}")
    public User getUserById(
            @PathVariable Long id) {

        return userService.getUserById(id);
    }


    @PutMapping("/{id}")
    public User updateUser(
            @PathVariable Long id,
            @RequestBody User user) {

        return userService.updateUser(
            id,
            user
        );
    }


    @DeleteMapping("/{id}")
    public void deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);
    }

}