package com.examly.springapp.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examly.springapp.model.BackgroundCheck;
import com.examly.springapp.repository.BackgroundCheckRepository;
import com.examly.springapp.service.BackgroundCheckService;

@Service
public class BackgroundCheckServiceImpl implements BackgroundCheckService {

  @Autowired
  private BackgroundCheckRepository repository;

  @Override
  public BackgroundCheck saveBackgroundCheck(BackgroundCheck backgroundCheck) {
    return repository.save(backgroundCheck);
  }

  @Override
  public List<BackgroundCheck> getAllBackgroundChecks() {
    return repository.findAll();
  }

  @Override
  public BackgroundCheck getBackgroundCheckById(Long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public BackgroundCheck updateBackgroundCheck(Long id, BackgroundCheck backgroundCheck) {

    BackgroundCheck existing = repository.findById(id).orElse(null);

    if(existing != null){
      existing.setCheckType(backgroundCheck.getCheckType());
      existing.setStatus(backgroundCheck.getStatus());
      existing.setCompletionDate(backgroundCheck.getCompletionDate());
      existing.setResult(backgroundCheck.getResult());
      existing.setRemarks(backgroundCheck.getRemarks());
      existing.setVerificationAgency(backgroundCheck.getVerificationAgency());

      return repository.save(existing);
    }

    return null;
  }

  @Override
  public void deleteBackgroundCheck(Long id) {
    repository.deleteById(id);
  }
}
