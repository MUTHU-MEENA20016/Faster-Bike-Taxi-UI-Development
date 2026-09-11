package com.examly.springapp.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examly.springapp.model.Vehicle;
import com.examly.springapp.repository.VehicleRepository;
import com.examly.springapp.service.VehicleService;

@Service
public class VehicleServiceImpl implements VehicleService {

  @Autowired
  private VehicleRepository repository;

  @Override
  public Vehicle saveVehicle(Vehicle vehicle) {
    return repository.save(vehicle);
  }

  @Override
  public List<Vehicle> getAllVehicles() {
    return repository.findAll();
  }

  @Override
  public Vehicle getVehicleById(Long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public Vehicle updateVehicle(Long id, Vehicle vehicle) {

    Vehicle existing = repository.findById(id).orElse(null);

    if(existing != null){
      existing.setRegistrationNumber(vehicle.getRegistrationNumber());
      existing.setMake(vehicle.getMake());
      existing.setModel(vehicle.getModel());
      existing.setYearOfManufacture(vehicle.getYearOfManufacture());
      existing.setInsurancePolicyNumber(vehicle.getInsurancePolicyNumber());
      existing.setInsuranceExpiryDate(vehicle.getInsuranceExpiryDate());
      existing.setPollutionCertificateNumber(vehicle.getPollutionCertificateNumber());
      existing.setPollutionCertificateExpiry(vehicle.getPollutionCertificateExpiry());
      existing.setLastInspectionDate(vehicle.getLastInspectionDate());
      existing.setVehicleCondition(vehicle.getVehicleCondition());

      return repository.save(existing);
    }

    return null;
  }

  @Override
  public void deleteVehicle(Long id) {
    repository.deleteById(id);
  }
}
