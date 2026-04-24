package com.healthapp.itemhealth.model;

import jakarta.validation.constraints.NotNull;
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
  // primary key
  private Long carId;
  // foreign key
  @NotNull private int carYear;
  private int milage;
  private boolean toReplace;
  private LocalDate lastServiced;
  private boolean toService;
  @Builder.Default private LocalDate needToServiceDate = LocalDate.now().plusYears(2);
  private LocalDate lastInsuranceRenewal;
  @Builder.Default private LocalDate insuranceExpireDate = LocalDate.now().plusYears(2);
  private boolean toRenewInsurance;
  private boolean inUse;
  @NotNull private Long employeeId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Employee employee;
}
