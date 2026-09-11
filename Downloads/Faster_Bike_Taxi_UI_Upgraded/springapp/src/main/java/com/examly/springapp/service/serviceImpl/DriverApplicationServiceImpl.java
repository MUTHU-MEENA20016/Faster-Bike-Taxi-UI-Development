package com.examly.springapp.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examly.springapp.model.DriverApplication;
import com.examly.springapp.repository.DriverApplicationRepository;
import com.examly.springapp.service.DriverApplicationService;

@Service
public class DriverApplicationServiceImpl implements DriverApplicationService {

  @Autowired
  private DriverApplicationRepository repository;

  @Override
  public DriverApplication saveApplication(DriverApplication application) {
    return repository.save(application);
  }

  @Override
  public List<DriverApplication> getAllApplications() {
    return repository.findAll();
  }

  @Override
  public DriverApplication getApplicationById(Long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public DriverApplication updateApplication(Long id, DriverApplication application) {

    DriverApplication existing = repository.findById(id).orElse(null);

    if(existing != null){
      existing.setName(application.getName());
      existing.setBikeNumber(application.getBikeNumber());
      existing.setAge(application.getAge());
      existing.setPhoneNumber(application.getPhoneNumber());
      existing.setAddress(application.getAddress());
      existing.setEmergencyContact(application.getEmergencyContact());
      existing.setDrivingLicenseNumber(application.getDrivingLicenseNumber());
      existing.setStatus(application.getStatus());
      existing.setVerificationStage(application.getVerificationStage());

      return repository.save(existing);
    }

    return null;
  }

  @Override
  public void deleteApplication(Long id) {
    repository.deleteById(id);
  }
}

