package com.examly.springapp.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examly.springapp.model.User;
import com.examly.springapp.repository.UserRepository;
import com.examly.springapp.service.UserService;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public User saveUser(User user) {
    return userRepository.save(user);
  }

  @Override
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public User getUserById(Long id) {
    return userRepository.findById(id).orElse(null);
  }

  @Override
  public User updateUser(Long id, User user) {

    User existingUser = userRepository.findById(id).orElse(null);

    if (existingUser != null) {
      existingUser.setUsername(user.getUsername());
      existingUser.setEmail(user.getEmail());
      existingUser.setPasswordHash(user.getPasswordHash());
      existingUser.setPhoneNumber(user.getPhoneNumber());
      existingUser.setRole(user.getRole());
      existingUser.setIsActive(user.getIsActive());
      existingUser.setProfileComplete(user.getProfileComplete());

      return userRepository.save(existingUser);
    }

    return null;
  }

  @Override
  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }
}
