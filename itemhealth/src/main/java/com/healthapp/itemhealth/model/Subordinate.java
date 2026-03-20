package com.healthapp.itemhealth.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subordinate {
  @NotBlank
  private Long bossId;
  @NotBlank
  private Long subordinateId;
}
