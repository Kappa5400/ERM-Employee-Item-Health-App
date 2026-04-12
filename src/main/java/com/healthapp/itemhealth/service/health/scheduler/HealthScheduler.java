package com.healthapp.itemhealth.service.health.scheduler;

import com.healthapp.itemhealth.service.HealthCheckService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HealthScheduler {

  private final HealthCheckService healthCheckService;

  public HealthScheduler(HealthCheckService healthCheckService) {
    this.healthCheckService = healthCheckService;
  }

  @Scheduled(cron = "0 0 8 * * *")
  public void runHealthCheck() {
    healthCheckService.runHealthCheck();
  }
}
