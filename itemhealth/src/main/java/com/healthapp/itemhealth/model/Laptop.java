package com.healthapp.itemhealth.model;

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
public class Laptop {
  private long laptopId;
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
