package com.healthapp.itemhealth.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Laptop {
  @JsonProperty("laptopId")
  private Long LaptopId;
  @Positive private int osVersion;
  private LocalDateTime lastOSUpdate;
  private boolean needToUpdate;
  // make default for laptopYear so not null for health logic
  @NotNull private int laptopYear;
  private boolean toRenew;
  private boolean inUse;
  @NotNull private long employeeId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Employee employee;
}
