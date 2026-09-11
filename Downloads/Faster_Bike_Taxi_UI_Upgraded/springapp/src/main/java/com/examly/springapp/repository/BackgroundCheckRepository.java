package com.examly.springapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.examly.springapp.model.BackgroundCheck;

public interface BackgroundCheckRepository
    extends JpaRepository<BackgroundCheck, Long> {

  List<BackgroundCheck> findByStatus(
      BackgroundCheck.BackgroundCheckStatus status);

  List<BackgroundCheck> findByCheckType(
      BackgroundCheck.BackgroundCheckType checkType);
}