package com.examly.springapp.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examly.springapp.model.DriverPerformance;
import com.examly.springapp.repository.DriverPerformanceRepository;
import com.examly.springapp.service.DriverPerformanceService;

@Service
public class DriverPerformanceServiceImpl implements DriverPerformanceService {

  @Autowired
  private DriverPerformanceRepository repository;

  @Override
  public DriverPerformance savePerformance(DriverPerformance performance) {
    return repository.save(performance);
  }

  @Override
  public List<DriverPerformance> getAllPerformances() {
    return repository.findAll();
  }

  @Override
  public DriverPerformance getPerformanceById(Long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public DriverPerformance updatePerformance(Long id, DriverPerformance performance) {

    DriverPerformance existing = repository.findById(id).orElse(null);

    if(existing != null){
      existing.setTotalRides(performance.getTotalRides());
      existing.setCompletedRides(performance.getCompletedRides());
      existing.setCancelledRides(performance.getCancelledRides());
      existing.setAverageRating(performance.getAverageRating());
      existing.setTotalEarnings(performance.getTotalEarnings());
      existing.setLastActiveDate(performance.getLastActiveDate());
      existing.setPerformanceScore(performance.getPerformanceScore());
      existing.setStatus(performance.getStatus());

      return repository.save(existing);
    }

    return null;
  }

  @Override
  public void deletePerformance(Long id) {
    repository.deleteById(id);
  }
}
