package com.healthapp.itemhealth.model;

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
public class IDCard {
  private long idCardId;
  private long employeeId;
  private LocalDate lastRenewedDate;
  private LocalDate needToRenewDate;
  private boolean inUse;
  private boolean toRenew;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Employee employee;
}
