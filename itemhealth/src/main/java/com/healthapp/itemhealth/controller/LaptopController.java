package com.healthapp.itemhealth.controller;

import com.healthapp.itemhealth.model.Laptop;
import com.healthapp.itemhealth.service.LaptopService;
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
@RequestMapping("/api/laptop")
@Validated
public class LaptopController {

  private static final Logger log = LoggerFactory.getLogger(LaptopController.class);
  private final LaptopService laptopService;

  public LaptopController(LaptopService laptopService) {
    this.laptopService = laptopService;
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    StringTrimmerEditor stringTrimmer = new StringTrimmerEditor(true);
    binder.registerCustomEditor(String.class, stringTrimmer);
  }

  @GetMapping("/{laptopId}")
  public ResponseEntity<Laptop> getById(@PathVariable Long laptopId) {
    log.info("Fetching laptop with ID: {}", laptopId);
    return ResponseEntity.ok(laptopService.getById(laptopId));
  }

  @GetMapping
  public ResponseEntity<List<Laptop>> getAll() {
    log.info("Fetching all laptops");
    return ResponseEntity.ok(laptopService.getAll());
  }

  @GetMapping("/employee/{employeeId}")
  public ResponseEntity<Laptop> getByEmployeeId(@PathVariable Long employeeId) {
    log.info("Fetching laptop for employee ID: {}", employeeId);
    return ResponseEntity.ok(laptopService.getByEmployeeId(employeeId));
  }

  @GetMapping("/need-to-update")
  public ResponseEntity<List<Laptop>> getNeedToUpdate() {
    log.info("Fetching laptops requiring updates");
    return ResponseEntity.ok(laptopService.getNeedToUpdate());
  }

  @GetMapping("/in-use")
  public ResponseEntity<List<Laptop>> getInUse() {
    log.info("Fetching all laptops currently in use");
    return ResponseEntity.ok(laptopService.getInUse());
  }

  @GetMapping("/to-renew")
  public ResponseEntity<List<Laptop>> getToRenew() {
    log.info("Fetching laptops requiring insurance/warranty renewal");
    return ResponseEntity.ok(laptopService.getToRenew());
  }

  @PostMapping
  public ResponseEntity<Laptop> create(@Valid @RequestBody Laptop laptop) {

    if (laptop == null) {
      return ResponseEntity.badRequest().build();
    }

    log.info("Creating new laptop record: {}", laptop);
    laptopService.create(laptop);
    return ResponseEntity.status(201).body(laptop);
  }

  @PatchMapping
  public ResponseEntity<Void> update(@Valid @RequestBody Laptop laptop) {
    log.info("Updating laptop record: {}", laptop);
    laptopService.update(laptop);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{laptopId}")
  public ResponseEntity<Void> delete(@PathVariable Long laptopId) {
    log.info("Deleting laptop with ID: {}", laptopId);
    laptopService.delete(laptopId);
    return ResponseEntity.noContent().build();
  }
}
