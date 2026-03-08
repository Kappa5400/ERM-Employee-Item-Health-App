package com.healthapp.itemhealth.model;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class IDCard {
    private long employee_ID;
    private Date last_renewed_date;
    private Date need_to_renew_date;
    private boolean in_use;
    private boolean to_renew;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
