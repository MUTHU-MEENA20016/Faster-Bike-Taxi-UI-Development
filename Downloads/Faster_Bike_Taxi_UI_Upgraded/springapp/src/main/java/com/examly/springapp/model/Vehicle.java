package com.examly.springapp.model;

import java.time.LocalDate;
import javax.persistence.*;
@Entity
@Table(name = "vehicles")
public class Vehicle {

  public enum VehicleCondition{
  EXCELLENT,
  GOOD,
  FAIR,
  NEEDS_REPAIR
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String registrationNumber;

  private String make;

  private String model;

  private Integer yearOfManufacture;

  private String insurancePolicyNumber;

  private LocalDate insuranceExpiryDate;

  private String pollutionCertificateNumber;

  private LocalDate pollutionCertificateExpiry;

  private LocalDate lastInspectionDate;

  @Enumerated(EnumType.STRING)
  private VehicleCondition vehicleCondition;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getRegistrationNumber() {
    return registrationNumber;
  }

  public void setRegistrationNumber(String registrationNumber) {
    this.registrationNumber = registrationNumber;
  }

  public String getMake() {
    return make;
  }

  public void setMake(String make) {
    this.make = make;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public Integer getYearOfManufacture() {
    return yearOfManufacture;
  }

  public void setYearOfManufacture(Integer yearOfManufacture) {
    this.yearOfManufacture = yearOfManufacture;
  }

  public String getInsurancePolicyNumber() {
    return insurancePolicyNumber;
  }

  public void setInsurancePolicyNumber(String insurancePolicyNumber) {
    this.insurancePolicyNumber = insurancePolicyNumber;
  }

  public LocalDate getInsuranceExpiryDate() {
    return insuranceExpiryDate;
  }

  public void setInsuranceExpiryDate(LocalDate insuranceExpiryDate) {
    this.insuranceExpiryDate = insuranceExpiryDate;
  }

  public String getPollutionCertificateNumber() {
    return pollutionCertificateNumber;
  }

  public void setPollutionCertificateNumber(String pollutionCertificateNumber) {
    this.pollutionCertificateNumber = pollutionCertificateNumber;
  }

  public LocalDate getPollutionCertificateExpiry() {
    return pollutionCertificateExpiry;
  }

  public void setPollutionCertificateExpiry(LocalDate pollutionCertificateExpiry) {
    this.pollutionCertificateExpiry = pollutionCertificateExpiry;
  }

  public LocalDate getLastInspectionDate() {
    return lastInspectionDate;
  }

  public void setLastInspectionDate(LocalDate lastInspectionDate) {
    this.lastInspectionDate = lastInspectionDate;
  }

  public VehicleCondition getVehicleCondition() {
    return vehicleCondition;
  }

  public void setVehicleCondition(VehicleCondition vehicleCondition) {
    this.vehicleCondition = vehicleCondition;
  }

  @OneToOne
@JoinColumn(name = "application_id")
private DriverApplication application;

public DriverApplication getApplication() {
  return application;
}

public void setApplication(DriverApplication application) {
  this.application = application;
}
}

