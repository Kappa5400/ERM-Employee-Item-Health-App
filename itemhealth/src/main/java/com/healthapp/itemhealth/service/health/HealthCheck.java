package com.healthapp.itemhealth.service.health;

public interface HealthCheck <T>{

    public boolean checkUpdate(T item);
    void performUpdate(T item);

}
