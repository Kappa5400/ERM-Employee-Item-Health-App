package com.healthapp.itemhealth.service.health.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.healthapp.itemhealth.service.HealthCheckService;

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

  
  @Scheduled(fixedRate= 604800000)
  public void runCleanup(){
    healthCheckService.cleanup();
  }
}
