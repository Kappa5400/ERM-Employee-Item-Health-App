package com.healthapp.itemhealth.model;

import java.time.LocalDate;
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
  //Primary key
  @JsonProperty("laptopId")
  private Long LaptopId;
  @Positive private int osVersion;
  private LocalDate lastOSUpdate;
  private boolean needToUpdate;
  
  @NotNull private int laptopYear;
  private boolean toRenew;
  private boolean inUse;
  // Foreign key
  @NotNull private long employeeId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Employee employee;
}
