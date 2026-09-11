package com.examly.springapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.examly.springapp.model.DriverPerformance;
import com.examly.springapp.service.DriverPerformanceService;

@RestController
@RequestMapping("/api/performance")
@CrossOrigin("*")
public class DriverPerformanceController {

  @Autowired
  private DriverPerformanceService service;

  @PostMapping
  public DriverPerformance savePerformance(
      @RequestBody DriverPerformance performance){
    return service.savePerformance(performance);
  }

  @GetMapping
  public List<DriverPerformance> getAllPerformances(){
    return service.getAllPerformances();
  }

  @GetMapping("/{id}")
  public DriverPerformance getPerformanceById(
      @PathVariable Long id){
    return service.getPerformanceById(id);
  }

  @PutMapping("/{id}")
  public DriverPerformance updatePerformance(
      @PathVariable Long id,
      @RequestBody DriverPerformance performance){
    return service.updatePerformance(id, performance);
  }

  @DeleteMapping("/{id}")
  public void deletePerformance(@PathVariable Long id){
    service.deletePerformance(id);
  }
}
