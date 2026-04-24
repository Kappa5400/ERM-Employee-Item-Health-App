package com.healthapp.itemhealth.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// The subordinate table is used to link employees to bosses, important
// for some crud logic when an employee needs to have a boss.

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subordinate {
  // Foreign key
  @NotBlank private Long bossId;
  // Primary key
  @NotBlank private Long subordinateId;
}
