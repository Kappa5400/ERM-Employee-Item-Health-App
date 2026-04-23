package com.healthapp.itemhealth.model;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
  private Long employeeId;
  @NotBlank(message = "Must enter a name")
  private String name;
  @NotBlank(message = "Must enter a title")
  private String title;
  @NotNull(message = "Boss must be selected")
  private Long bossUserId;
  
  private boolean bossRole;
  
  private boolean hasBoss;
  
  private LocalDateTime createdAt;
  
  private LocalDateTime updatedAt;

  @NotBlank(message = "Username cannot be empty")
  private String username;

  @NotBlank(message = "Password cannot be empty")
  @Size(min = 8, message = "Password must be at least 8 characters")
  private String password;

  private Boss boss;
  private String email;
}
