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
public class Laptop {
  private long laptop_id;
  private String os_version;
  private boolean need_to_update;
  private int laptop_year;
  private boolean to_renew;
  private boolean in_use;
  private long employee_ID;
  private LocalDateTime created_at;
  private LocalDateTime updated_at;
}
