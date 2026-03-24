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
public class IDCard {
  @NotNull private long idCardId;
  @NotNull private long employeeId;
  @Builder.Default private LocalDate lastRenewedDate = LocalDate.now();
  @Builder.Default private LocalDate needToRenewDate = LocalDate.now().plusYears(2);
  private boolean inUse;
  private boolean toRenew;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Employee employee;
}
