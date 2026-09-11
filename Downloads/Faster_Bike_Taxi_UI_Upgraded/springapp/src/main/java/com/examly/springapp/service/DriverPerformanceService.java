package com.examly.springapp.service;
import java.util.List;
import com.examly.springapp.model.DriverPerformance;

public interface DriverPerformanceService {

  DriverPerformance savePerformance(
      DriverPerformance performance);

  List<DriverPerformance> getAllPerformances();

  DriverPerformance getPerformanceById(Long id);

  DriverPerformance updatePerformance(
      Long id,
      DriverPerformance performance);

  void deletePerformance(Long id);
}