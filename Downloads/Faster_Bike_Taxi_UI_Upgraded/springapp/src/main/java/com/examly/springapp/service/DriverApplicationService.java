package com.examly.springapp.service;

import java.util.List;
import com.examly.springapp.model.DriverApplication;

public interface DriverApplicationService {

  DriverApplication saveApplication(DriverApplication application);

  List<DriverApplication> getAllApplications();

  DriverApplication getApplicationById(Long id);

  DriverApplication updateApplication(Long id,
                    DriverApplication application);

  void deleteApplication(Long id);
}
