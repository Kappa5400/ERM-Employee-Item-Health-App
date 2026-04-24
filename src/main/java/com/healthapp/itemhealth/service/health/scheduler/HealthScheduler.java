package com.healthapp.itemhealth.service.health.scheduler;

import com.healthapp.itemhealth.service.HealthCheckService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// The scheduler for the script, fixed rate
// triggers the health check every day, and the cleanup every
// 5 days.

@Component
public class HealthScheduler {

  private final HealthCheckService healthCheckService;

  public HealthScheduler(HealthCheckService healthCheckService) {
    this.healthCheckService = healthCheckService;
  }

  @Scheduled(fixedRate = 86400000)
  public void runHealthCheck() {
    healthCheckService.runHealthCheck();
  }

  @Scheduled(fixedRate = 604800000)
  public void runCleanup() {
    healthCheckService.cleanup();
  }
}
