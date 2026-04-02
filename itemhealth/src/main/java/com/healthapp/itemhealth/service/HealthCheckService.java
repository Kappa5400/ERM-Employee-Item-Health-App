package com.healthapp.itemhealth.service;

import com.healthapp.itemhealth.model.Car;
import com.healthapp.itemhealth.model.Employee;
import com.healthapp.itemhealth.model.IDCard;
import com.healthapp.itemhealth.model.Laptop;
import com.healthapp.itemhealth.model.HealthReport;
import com.healthapp.itemhealth.service.health.CarHealth;
import com.healthapp.itemhealth.service.health.IDCardHealth;
import com.healthapp.itemhealth.service.health.LaptopHealth;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HealthCheckService {

  private static final Logger log = LoggerFactory.getLogger(HealthCheckService.class);

  private final EmailService emailService;
  private final EmployeeService employeeService;
  private final LaptopService laptopService;
  private final CarService carService;
  private final IDCardService idCardService;
  private final LaptopHealth laptopHealth;
  private final CarHealth carHealth;
  private final IDCardHealth idCardHealth;

  public HealthCheckService(
      EmailService emailService,
      EmployeeService employeeService,
      LaptopService laptopService,
      CarService carService,
      IDCardService idCardService,
      LaptopHealth laptopHealth,
      CarHealth carHealth,
      IDCardHealth idCardHealth) {
    this.emailService = emailService;
    this.employeeService = employeeService;
    this.laptopService = laptopService;
    this.carService = carService;
    this.idCardService = idCardService;
    this.laptopHealth = laptopHealth;
    this.carHealth = carHealth;
    this.idCardHealth = idCardHealth;
  }

  public void runHealthCheck() {

    
    log.info("Starting health check");
    log.info("Fetching all employees...");
    List<Employee> allEmployees = employeeService.findAllEmployees();
    log.info("Done.");
    log.info("Looping through employees...");

    for (Employee employee : allEmployees) {
      Long employeeId = employee.getEmployeeId();

      List<HealthReport> updateItems = new ArrayList<>();

      Laptop laptop = laptopService.getByEmployeeId(employeeId);
      Car car = carService.getByEmployeeId(employeeId);
      IDCard idCard = idCardService.getByEmployeeId(employeeId);

      log.info("Checking laptops...");
      if (laptop != null) {

        if (laptopHealth.checkUpdate(laptop)) {
          log.info("To update laptop found. Performing update...");
          laptopHealth.performUpdate(laptop);
          updateItems.add(HealthReport.builder()
          .employee(employee)
          .laptop(laptop)
          .itemType("Laptop")
          .build()
        );
          
        }
      }
      log.info("Laptop check done.");
      log.info("Checking cars....");
      if (car != null) {
        if (carHealth.checkUpdate(car)) {
          log.info("To update car found. Performing update...");
          carHealth.performUpdate(car);
          updateItems.add(HealthReport.builder()
          .employee(employee)
          .car(car)
          .itemType("Car")
          .build()
        );
        }
      }
      log.info("Car check done.");
      log.info("Checking IDs...");
      if (idCard != null) {
        if (idCardHealth.checkUpdate(idCard)) {
          log.info("To update ID found. Performing update...");
          idCardHealth.performUpdate(idCard);
          updateItems.add(HealthReport.builder()
          .employee(employee)
          .idCard(idCard)
          .itemType("IDCard")
          .build()
        );
        }
      }
      log.info("ID check done.");

      log.info(updateItems.size() + " items to update.");

      if (updateItems.size() != 0) {

        log.info("Getting boss email...");
        String emailAddress = emailService.getBossEmail(employee);
        String emailSubject = emailService.formatEmailSubject(employee);
        String emailBody = emailService.formatEmailBody(updateItems);

        log.info("Sending email...");
        emailService.sendEmail(emailAddress, emailSubject, emailBody);
        log.info("Email sent.");
      } else {
        log.info("No items to update.");
      }


      log.info("Healthcheck finished.");
    }
  }
}
