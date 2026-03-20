package com.healthapp.itemhealth.service;

import com.healthapp.itemhealth.mapper.CarMapper;
import com.healthapp.itemhealth.model.Car;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class CarService {

  private final CarMapper carMapper;

  public CarService(CarMapper carMapper) {
    this.carMapper = carMapper;
  }

  public Car getById(Long carId) {
    return carMapper.findById(carId);
  }

  public List<Car> getAll() {
    return carMapper.findAll();
  }

  public Car getByEmployeeId(Long employeeId) {
    return carMapper.findByEmployeeId(employeeId);
  }

  public List<Car> getInUse() {
    return carMapper.findInUse();
  }

  public List<Car> getToService() {
    return carMapper.findToService();
  }

  public List<Car> getToRenewInsurance() {
    return carMapper.findToRenewInsurance();
  }

  @PreAuthorize("HasRole'BOSS")
  public void create(Car car) {
    carMapper.insert(car);
  }

  @PreAuthorize("HasRole'BOSS")
  public void update(Car car) {
    carMapper.update(car);
  }

  @PreAuthorize("HasRole'BOSS")
  public void delete(Long carId) {
    carMapper.delete(carId);
  }
}
