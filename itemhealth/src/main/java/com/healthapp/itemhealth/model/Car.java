package com.healthapp.itemhealth.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.healthapp.itemhealth.model.Employee;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Car {
  private long carId;
  private int carYear;
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
