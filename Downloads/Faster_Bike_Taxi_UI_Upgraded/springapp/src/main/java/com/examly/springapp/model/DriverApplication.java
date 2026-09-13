package com.examly.springapp.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "driver_applications")
public class DriverApplication {

    public enum ApplicationStatus {
        DRAFT,
        SUBMITTED,
        UNDER_REVIEW,
        DOCUMENT_VERIFICATION,
        BACKGROUND_CHECK,
        APPROVED,
        REJECTED,
        ON_HOLD
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String bikeNumber;

    private Integer age;

    private String phoneNumber;

    private String address;

    private String emergencyContact;

    @Column(unique = true)
    private String drivingLicenseNumber;

    /*
     * This is the username of the authenticated applicant.
     * The backend sets this value from the JWT principal.
     */
    private String applicantUsername;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private String verificationStage;

    private String rejectionReason;

    private LocalDateTime submissionDate;

    private LocalDateTime lastModified;

    @ManyToOne
    @JoinColumn(name = "assigned_reviewer_id")
    @JsonIgnore
    private User assignedReviewer;

    @OneToMany(mappedBy = "application")
    @JsonIgnore
    private List<Document> documents;

    @OneToMany(mappedBy = "application")
    @JsonIgnore
    private List<BackgroundCheck> backgroundChecks;

    @OneToOne(mappedBy = "application")
    @JsonIgnore
    private Vehicle vehicle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBikeNumber() {
        return bikeNumber;
    }

    public void setBikeNumber(String bikeNumber) {
        this.bikeNumber = bikeNumber;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getDrivingLicenseNumber() {
        return drivingLicenseNumber;
    }

    public void setDrivingLicenseNumber(String drivingLicenseNumber) {
        this.drivingLicenseNumber = drivingLicenseNumber;
    }

    public String getApplicantUsername() {
        return applicantUsername;
    }

    public void setApplicantUsername(String applicantUsername) {
        this.applicantUsername = applicantUsername;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public String getVerificationStage() {
        return verificationStage;
    }

    public void setVerificationStage(String verificationStage) {
        this.verificationStage = verificationStage;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public User getAssignedReviewer() {
        return assignedReviewer;
    }

    public void setAssignedReviewer(User assignedReviewer) {
        this.assignedReviewer = assignedReviewer;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public List<BackgroundCheck> getBackgroundChecks() {
        return backgroundChecks;
    }

    public void setBackgroundChecks(List<BackgroundCheck> backgroundChecks) {
        this.backgroundChecks = backgroundChecks;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}