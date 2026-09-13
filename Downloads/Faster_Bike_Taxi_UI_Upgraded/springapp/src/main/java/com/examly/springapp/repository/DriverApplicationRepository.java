package com.examly.springapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.examly.springapp.model.DriverApplication;

public interface DriverApplicationRepository extends JpaRepository<DriverApplication, Long> {

    List<DriverApplication> findByStatus(DriverApplication.ApplicationStatus status);

    DriverApplication findByBikeNumber(String bikeNumber);

    DriverApplication findByDrivingLicenseNumber(String drivingLicenseNumber);

    List<DriverApplication> findByApplicantUsernameIgnoreCase(String username);

    DriverApplication findByPhoneNumber(String phoneNumber);

    long countByStatus(DriverApplication.ApplicationStatus status);
}