package com.examly.springapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.examly.springapp.model.BackgroundCheck;
import com.examly.springapp.service.BackgroundCheckService;

@RestController
@RequestMapping("/api/background-checks")
@CrossOrigin("*")
public class BackgroundCheckController {

  @Autowired
  private BackgroundCheckService service;

  @PostMapping
  public BackgroundCheck saveBackgroundCheck(
      @RequestBody BackgroundCheck backgroundCheck){
    return service.saveBackgroundCheck(backgroundCheck);
  }

  @GetMapping
  public List<BackgroundCheck> getAllBackgroundChecks(){
    return service.getAllBackgroundChecks();
  }

  @GetMapping("/{id}")
  public BackgroundCheck getBackgroundCheckById(
      @PathVariable Long id){
    return service.getBackgroundCheckById(id);
  }

  @PutMapping("/{id}")
  public BackgroundCheck updateBackgroundCheck(
      @PathVariable Long id,
      @RequestBody BackgroundCheck backgroundCheck){
    return service.updateBackgroundCheck(id, backgroundCheck);
  }

  @DeleteMapping("/{id}")
  public void deleteBackgroundCheck(@PathVariable Long id){
    service.deleteBackgroundCheck(id);
  }
}
