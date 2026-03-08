package com.healthapp.itemhealth.model;

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
    private Long employeeId;
    private List<Long> subordinateIds;   
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
