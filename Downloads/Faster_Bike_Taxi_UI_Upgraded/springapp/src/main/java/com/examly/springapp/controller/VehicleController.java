package com.examly.springapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.examly.springapp.model.Vehicle;
import com.examly.springapp.service.VehicleService;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin("*")
public class VehicleController {

  @Autowired
  private VehicleService service;

  @PostMapping
  public Vehicle saveVehicle(@RequestBody Vehicle vehicle){
    return service.saveVehicle(vehicle);
  }

  @GetMapping
  public List<Vehicle> getAllVehicles(){
    return service.getAllVehicles();
  }

  @GetMapping("/{id}")
  public Vehicle getVehicleById(@PathVariable Long id){
    return service.getVehicleById(id);
  }

  @PutMapping("/{id}")
  public Vehicle updateVehicle(@PathVariable Long id,
                 @RequestBody Vehicle vehicle){
    return service.updateVehicle(id, vehicle);
  }

  @DeleteMapping("/{id}")
  public void deleteVehicle(@PathVariable Long id){
    service.deleteVehicle(id);
  }
}
