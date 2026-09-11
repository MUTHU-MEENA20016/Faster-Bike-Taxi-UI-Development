package com.examly.springapp.service;

import java.util.List;
import com.examly.springapp.model.Vehicle;

public interface VehicleService {

  Vehicle saveVehicle(Vehicle vehicle);

  List<Vehicle> getAllVehicles();

  Vehicle getVehicleById(Long id);

  Vehicle updateVehicle(Long id, Vehicle vehicle);

  void deleteVehicle(Long id);
}