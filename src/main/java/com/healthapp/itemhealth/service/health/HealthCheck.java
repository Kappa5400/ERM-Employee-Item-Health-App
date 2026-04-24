package com.healthapp.itemhealth.service.health;

// An interface class that calls each item
// and calls the polymorphic method 
// perform update

public interface HealthCheck<T> {

  public boolean checkUpdate(T item);

  void performUpdate(T item);
}
