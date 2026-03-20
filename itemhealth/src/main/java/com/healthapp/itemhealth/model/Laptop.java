package com.healthapp.itemhealth.model;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Laptop {
  @NotBlank
  private long laptopId;
  @NotBlank
  private int osVersion;
  private LocalDateTime lastOSUpdate;
  private boolean needToUpdate;
  private int laptopYear;
  private boolean toRenew;
  private boolean inUse;
  private long employeeId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Employee employee;
}
