package com.cbs.Driver.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name="drivers")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3,max = 20, message = "Name must be between 3 and 20 digits")
    @Column(nullable = false, length = 50)
    @NotBlank(message = "Name number cannot be empty or null")
    private String firstName;

    @Size(min = 3,max = 20, message = "Name must be between 3 and 20 digits")
    @NotBlank(message = "lastName number cannot be empty or null")
    @Column(nullable = false, length = 50)
    private String lastName;

    @NotBlank(message = "email number cannot be empty or null")
    @Column(nullable = false, unique = true, length = 100)
    @Email(message = "Email should be a valid email address")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password number cannot be empty or null")
    private String passwordHash;

    @Size(min = 10, max = 10, message = "Phone number must be between 10 and 20 digits")
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Phone number cannot be empty or null")
    private String phoneNumber;

    @NotBlank(message = "license number cannot be empty or null")
    @Column(nullable = false, unique = true, length = 50)
    private String licenseNumber;

    @NotBlank(message = "Vehicle Model cannot be empty or null")
    @Column(nullable = false, length = 50)
    private String vehicleModel;

    @NotBlank(message = "licensePlate cannot be empty or null")
    @Column(nullable = false, length = 20)
    private String licensePlate;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    public  DriverStatus  status;

    public enum DriverStatus{
        AVAILABLE,    // Driver is online and ready to accept rides.
        ON_RIDE,      // Driver is currently on an active ride.
        OFFLINE,      // Driver is logged out or not available.
        BUSY,         // Driver is online but temporarily busy (e.g., break, fueling).
        SUSPENDED
    }

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }



    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }
    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getLicensePlate() {
        return licensePlate;
    }
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }
    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }



}
