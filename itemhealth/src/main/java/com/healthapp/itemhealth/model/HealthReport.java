package com.healthapp.itemhealth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthReport {
  private Employee employee;
  private Car car;
  private IDCard idCard;
  private Laptop laptop;
  private String itemType;
}
