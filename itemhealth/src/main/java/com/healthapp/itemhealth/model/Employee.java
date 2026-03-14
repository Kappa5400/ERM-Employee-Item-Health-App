package com.healthapp.itemhealth.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
  private long employee_id;
  private String name;
  private String title;
  private long boss_user_id;
  private boolean boss_role;
  private boolean has_boss;
  private LocalDateTime created_at;
  private LocalDateTime updated_at;
}
