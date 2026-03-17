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
  private Long employeeId;      
    private String name;
    private String title;
    private Long bossUserId;      
    private boolean bossRole;     
    private boolean hasBoss;     
    private LocalDateTime createdAt; 
    private LocalDateTime updatedAt;

    private Boss boss;
}
