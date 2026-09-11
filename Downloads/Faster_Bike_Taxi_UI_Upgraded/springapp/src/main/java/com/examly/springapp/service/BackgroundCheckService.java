package com.examly.springapp.service;

import java.util.List;
import com.examly.springapp.model.BackgroundCheck;

public interface BackgroundCheckService {

  BackgroundCheck saveBackgroundCheck(
      BackgroundCheck backgroundCheck);

  List<BackgroundCheck> getAllBackgroundChecks();

  BackgroundCheck getBackgroundCheckById(Long id);

  BackgroundCheck updateBackgroundCheck(
      Long id,
      BackgroundCheck backgroundCheck);

  void deleteBackgroundCheck(Long id);
}

