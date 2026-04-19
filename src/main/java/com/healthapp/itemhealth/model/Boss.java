package com.healthapp.itemhealth.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Boss {
  private Long bossId;

  @NotNull private Long employeeId;

  @NotBlank
  @Pattern(
      regexp = "^[a-zA-Z0-9 ]*$",
      message = "Only alphanumeric characters and spaces are allowed")
  private String name;

  private String title;
  private List<Long> subordinateIds;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
