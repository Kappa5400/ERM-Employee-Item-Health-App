package com.healthapp.itemhealth.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Laptop {
  @NotNull private long laptopId;
  @Positive private int osVersion;
  private LocalDateTime lastOSUpdate;
  private boolean needToUpdate;
  private int laptopYear;
  private boolean toRenew;
  private boolean inUse;
  @NotNull private long employeeId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Employee employee;
}
