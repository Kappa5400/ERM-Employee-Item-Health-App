package com.healthapp.itemhealth.controller;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

import com.healthapp.itemhealth.model.Car;
import com.healthapp.itemhealth.model.Employee;
import com.healthapp.itemhealth.model.IDCard;
import com.healthapp.itemhealth.model.Laptop;
import com.healthapp.itemhealth.service.CarService;
import com.healthapp.itemhealth.service.EmployeeService;
import com.healthapp.itemhealth.service.ExcelService;
import com.healthapp.itemhealth.service.IDCardService;
import com.healthapp.itemhealth.service.LaptopService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employee")
@Validated
public class EmployeeController {

  private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);
  private final EmployeeService employeeService;
  private final CarService carService;
  private final IDCardService idCardService;
  private final LaptopService laptopService;

  private final ExcelService excelService;

  public EmployeeController(
      EmployeeService employeeService,
      ExcelService excelService,
      CarService carService,
      IDCardService idCardService,
      LaptopService laptopService) {
    this.employeeService = employeeService;
    this.carService = carService;
    this.laptopService = laptopService;
    this.idCardService = idCardService;
    this.excelService = excelService;
  }

  // The init binder is in each controller
  // it sanatizes input to prevent sql injection attack
  // and clean up invisible characters
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

  @PostMapping()
  public ResponseEntity<Employee> insert(@Valid @RequestBody Employee employee) {
    log.info("Inserting new employee: {}", employee.getUsername());

    employeeService.insert(employee);
    return ResponseEntity.status(201).body(employee);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    log.info("Deleting employee with ID: {}", id);
    employeeService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/email/{id}")
  public ResponseEntity<String> get(@PathVariable Long id) {
    log.info("Getting email of employee ID {}", id);
    return ResponseEntity.ok(employeeService.getEmail(id));
  }

  @GetMapping("/download")
  public ResponseEntity<Resource> downloadEmployees() {
    List<Employee> employees = employeeService.findAllEmployees();
    List<Laptop> laptops = laptopService.getAll();
    List<IDCard> idcard = idCardService.getAll();
    List<Car> cars = carService.getAll();

    // Initiates input stream and passes all the employees, laptops, idcards, and car data to the excel service method
    ByteArrayInputStream in = excelService.employeesToExcel(employees, laptops, cars, idcard);

    // Outputs a new excel file
    InputStreamResource file = new InputStreamResource(in);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employees.xlsx")
        .contentType(
            MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .body(file);
  }

  

}
