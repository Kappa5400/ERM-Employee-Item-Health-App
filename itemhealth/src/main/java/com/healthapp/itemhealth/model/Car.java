package com.healthapp.itemhealth.model;

import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Car {
  private long carId;

  @Pattern(
      regexp = "^[a-zA-Z0-9 ]*$",
      message = "Only alphanumeric characters and spaces are allowed")
  private int carYear;

  @Pattern(
      regexp = "^[a-zA-Z0-9 ]*$",
      message = "Only alphanumeric characters and spaces are allowed")
  private int milage;

  private boolean toReplace;
  private LocalDate lastServiced;
  private boolean toService;
  private LocalDate needToServiceDate;
  private LocalDate lastInsuranceRenewal;
  private LocalDate insuranceExpireDate;
  private boolean toRenewInsurance;
  private boolean inUse;
  private long employeeId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Employee employee;
}
