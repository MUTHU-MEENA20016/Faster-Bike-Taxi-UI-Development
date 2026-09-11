package com.examly.springapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.examly.springapp.model.Vehicle;

public interface VehicleRepository
    extends JpaRepository<Vehicle, Long> {

  Vehicle findByRegistrationNumber(String registrationNumber);
}