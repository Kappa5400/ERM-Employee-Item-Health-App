package com.healthapp.itemhealth.controller;

import com.healthapp.itemhealth.model.Employee;
import com.healthapp.itemhealth.service.EmployeeService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/employee")
@Validated
public class EmployeeController {

  private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);
  private final EmployeeService employeeService;

  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    StringTrimmerEditor stringTrimmer = new StringTrimmerEditor(true);
    binder.registerCustomEditor(String.class, stringTrimmer);
  }

  @GetMapping("{id}")
  public ResponseEntity<Employee> getById(@PathVariable Long id) {
    log.info("Fetching employee with ID: {}", id);
    return ResponseEntity.ok(employeeService.getById(id));
  }

  @GetMapping
  public ResponseEntity<List<Employee>> findAllEmployees() {
    log.info("Fetching all employees");
    return ResponseEntity.ok(employeeService.findAllEmployees());
  }

  @GetMapping("/boss")
  public ResponseEntity<List<Employee>> findAllEmployeeasBoss() {
    log.info("Fetching all employees with boss privileges");
    return ResponseEntity.ok(employeeService.findAllEmployeesBoss());
  }

  @GetMapping("boss/{employeeId}")
  public ResponseEntity<Employee> findBossByEmployeeId(@PathVariable Long employeeId) {
    log.info("Fetching boss details for employee ID: {}", employeeId);
    return ResponseEntity.ok(employeeService.findBossByEmployeeId(employeeId));
  }

  @PostMapping
  public ResponseEntity<Void> insert(@Valid @RequestBody Employee employee) {
    log.info("Inserting new employee: {}", employee.getUsername());
    employeeService.insert(employee);
    return ResponseEntity.status(201).build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    log.info("Deleting employee with ID: {}", id);
    employeeService.delete(id);
    return ResponseEntity.noContent().build();
  }
}