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
public class Car {
  private long car_id;
  private int car_year;
  private int milage;
  private boolean to_replace;
  private Date last_serviced;
  private boolean to_service;
  private Date need_to_service_date;
  private Date last_insurance_renewal;
  private Date insurance_expire_date;
  private boolean to_renew_insurance;
  private boolean in_use;
  private long employee_ID;
  private LocalDateTime created_at;
  private LocalDateTime updated_at;
}
