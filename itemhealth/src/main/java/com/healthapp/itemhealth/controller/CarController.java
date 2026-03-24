package com.healthapp.itemhealth.controller;

import com.healthapp.itemhealth.model.Car;
import com.healthapp.itemhealth.service.CarService;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/car")
@Validated
public class CarController {

  private static final Logger log = LoggerFactory.getLogger(CarController.class);
  private final CarService carService;

  public CarController(CarService carService) {
    this.carService = carService;
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    StringTrimmerEditor stringTrimmer = new StringTrimmerEditor(true);
    binder.registerCustomEditor(String.class, stringTrimmer);
  }

  @GetMapping("/{carId}")
  public ResponseEntity<Car> getById(@PathVariable Long carId) {
    log.info("Fetching car with ID: {}", carId);
    return ResponseEntity.ok(carService.getById(carId));
  }

  @GetMapping
  public ResponseEntity<List<Car>> getAll() {
    log.info("Fetching all cars");
    return ResponseEntity.ok(carService.getAll());
  }

  @GetMapping("/employee/{employeeId}")
  public ResponseEntity<Car> getByEmployeeId(@PathVariable Long employeeId) {
    log.info("Fetching car for employee ID: {}", employeeId);
    return ResponseEntity.ok(carService.getByEmployeeId(employeeId));
  }

  @GetMapping("/in-use")
  public ResponseEntity<List<Car>> getInUse() {
    log.info("Fetching all cars currently in use");
    return ResponseEntity.ok(carService.getInUse());
  }

  @GetMapping("/to-service")
  public ResponseEntity<List<Car>> getToService() {
    log.info("Fetching cars requiring service");
    return ResponseEntity.ok(carService.getToService());
  }

  @GetMapping("/to-renew-insurance")
  public ResponseEntity<List<Car>> getToRenewInsurance() {
    log.info("Fetching cars requiring insurance renewal");
    return ResponseEntity.ok(carService.getToRenewInsurance());
  }

  @PostMapping
  public ResponseEntity<Car> create(@Valid @RequestBody Car car) {
    log.info("Creating new car record: {}", car);
    carService.create(car);
    return ResponseEntity.status(201).body(car);
  }

  @PatchMapping
  public ResponseEntity<Void> update(@Valid @RequestBody Car car) {
    log.info("Updating car record: {}", car);
    carService.update(car);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{carId}")
  public ResponseEntity<Void> delete(@PathVariable Long carId) {
    log.info("Deleting car with ID: {}", carId);
    carService.delete(carId);
    return ResponseEntity.noContent().build();
  }
}
