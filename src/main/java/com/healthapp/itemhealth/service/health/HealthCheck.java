package com.healthapp.itemhealth.service.health;

// An interface class that each item
// implements. Does checkupdate and then
// perform update

public interface HealthCheck<T> {

  public boolean checkUpdate(T item);

  void performUpdate(T item);
}
