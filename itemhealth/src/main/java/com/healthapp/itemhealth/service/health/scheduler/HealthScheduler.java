package com.healthapp.itemhealth.service.health.scheduler;

import com.healthapp.itemhealth.service.HealthCheckService;

@Component
public class HealthScheduler {

    private final HealthCheckService healthCheckService;

    public HealthScheduler(HealthCheckService healthCheckService){
        this.healthCheckService = healthCheckService;
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void runHealthCheck(){
        healthCheckService.runHealthCheck();
    }

}
