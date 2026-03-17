package com.healthapp.itemhealth.controller;

import com.healthapp.itemhealth.model.Car;
import com.healthapp.itemhealth.service.CarService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cars")
public class CarController {

  private final CarService carService;

  public CarController(CarService carService) {
    this.carService = carService;
  }

  @GetMapping("/{carId}")
  public ResponseEntity<Car> getById(@PathVariable Long carId) {
    return ResponseEntity.ok(carService.getById(carId));
  }

  @GetMapping
  public ResponseEntity<List<Car>> getAll() {
    return ResponseEntity.ok(carService.getAll());
  }

  @GetMapping("/employee/{employeeId}")
  public ResponseEntity<Car> getByEmployeeId(@PathVariable Long employeeId) {
    return ResponseEntity.ok(carService.getByEmployeeId(employeeId));
  }

  @GetMapping("/in-use")
  public ResponseEntity<List<Car>> getInUse() {
    return ResponseEntity.ok(carService.getInUse());
  }

  @GetMapping("/to-service")
  public ResponseEntity<List<Car>> getToService() {
    return ResponseEntity.ok(carService.getToService());
  }

  @GetMapping("/to-renew-insurance")
  public ResponseEntity<List<Car>> getToRenewInsurance() {
    return ResponseEntity.ok(carService.getToRenewInsurance());
  }

  @PostMapping
  public ResponseEntity<Void> create(@RequestBody Car car) {
    carService.create(car);
    return ResponseEntity.status(201).build();
  }

  @PatchMapping
  public ResponseEntity<Void> update(@RequestBody Car car) {
    carService.update(car);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{carId}")
  public ResponseEntity<Void> delete(@PathVariable Long carId) {
    carService.delete(carId);
    return ResponseEntity.noContent().build();
  }
}
