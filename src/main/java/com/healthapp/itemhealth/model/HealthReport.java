package com.healthapp.itemhealth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// The health report is used in the health service class to collect data on each object for the
// automated email.

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
