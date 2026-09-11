package com.examly.springapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.examly.springapp.model.DriverPerformance;

public interface DriverPerformanceRepository
    extends JpaRepository<DriverPerformance, Long> {
}