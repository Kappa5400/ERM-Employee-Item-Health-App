package com.healthapp.itemhealth.service;

import com.healthapp.itemhealth.mapper.CarMapper;
import com.healthapp.itemhealth.model.Car;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class CarService {

  private static final Logger log = LoggerFactory.getLogger(CarService.class);

  private final CarMapper carMapper;

  public CarService(CarMapper carMapper) {
    this.carMapper = carMapper;
  }

  public Car getById(Long carId) {
    log.info("Fetching car with ID: {}", carId);
    return carMapper.findById(carId);
  }

  public List<Car> getAll() {
    log.info("Fetching all cars");
    return carMapper.findAll();
  }

  public Car getByEmployeeId(Long employeeId) {
    log.info("Fetching car for employee ID: {}", employeeId);
    return carMapper.findByEmployeeId(employeeId);
  }

  public List<Car> getInUse() {
    log.info("Fetching cars currently in use");
    return carMapper.findInUse();
  }

  public List<Car> getToService() {
    log.info("Fetching cars needing service");
    return carMapper.findToService();
  }

  public List<Car> getToRenewInsurance() {
    log.info("Fetching cars needing insurance renewal");
    return carMapper.findToRenewInsurance();
  }

  @PreAuthorize("HasRole'BOSS")
  public void create(Car car) {
    log.info("Creating car record for employee: {}", car.getEmployeeId());
    carMapper.insert(car);
  }

  @PreAuthorize("HasRole'BOSS")
  public void update(Car car) {
    log.info("Updating car record with ID: {}", car.getCarId());
    carMapper.update(car);
  }

  @PreAuthorize("HasRole'BOSS")
  public void delete(Long carId) {
    log.info("Deleting car record with ID: {}", carId);
    carMapper.delete(carId);
  }
}
