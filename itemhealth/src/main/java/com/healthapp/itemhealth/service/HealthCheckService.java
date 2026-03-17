package com.healthapp.itemhealth.service.health;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import com.healthapp.itemhealth.model.Car;
import com.healthapp.itemhealth.model.Employee;
import com.healthapp.itemhealth.model.IdCard;
import com.healthapp.itemhealth.model.Laptop;
import com.healthapp.itemhealth.service.CarService;
import com.healthapp.itemhealth.service.EmployeeService;
import com.healthapp.itemhealth.service.IdCardService;
import com.healthapp.itemhealth.service.LaptopService;

@Service
public class HealthCheckService {

    private static final Logger log = LoggerFactory.getLogger(HealthCheckService.class);


    private final EmployeeService employeeService;  
    private final LaptopService laptopService;      
    private final CarService carService;            
    private final IdCardService idCardService;      
    private final LaptopHealth laptopHealth;       
    private final CarHealth carHealth;              
    private final IdCardHealth idCardHealth;        

    List updateItems;

    public HealthCheckService(EmployeeService employeeService,
                              LaptopService laptopService,
                              CarService carService,
                              IdCardService idCardService,
                              LaptopHealth laptopHealth,
                              CarHealth carHealth,
                              IdCardHealth idCardHealth) {
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
        List<Employee> allEmployees = employeeService.getAll();   
        log.info("Done.");
        log.info("Looping through employees...");

        for (Employee employee : allEmployees) {
            Long employeeId = employee.getEmployeeId();             

            Laptop laptop = laptopService.getByEmployeeId(employeeId);
            Car car = carService.getByEmployeeId(employeeId);
            IdCard idCard = idCardService.getByEmployeeId(employeeId); 

            log.info("Checking laptops...");
            if (laptop != null) {
                
                if (laptopHealth.checkUpdate(laptop)) {
                    log.info("To update laptop found. Performing update...");
                    laptopHealth.performUpdate(laptop);
                    updateItems.add("Laptop ID: " + laptop.getLaptop_id() + "Employee ID:" + laptop.getEmployee_ID());
                }
                
            }
            log.info("Laptop check done.");
            log.info("Checking cars....");
            if (car != null) {
                if (carHealth.checkUpdate(car)) {
                    log.info("To update car found. Performing update...");
                    carHealth.performUpdate(car);
                    updateItems.add("Car ID: " + car.getCar_id() + "Employee ID:" + car.getEmployee_ID());
                }
            }
            log.info("Car check done.");
            log.info("Checking IDs...");
            if (idCard != null) {
                if (idCardHealth.checkUpdate(idCard)) {
                    log.info("To update ID found. Performing update...");
                    idCardHealth.performUpdate(idCard);
                    updateItems.add("ID card ID: " + idCard.getid_card_id() + "Employee ID:" + idCard.getEmployee_ID());
                }
            }
            log.info("ID check done.");

            log.info(updateItems.size() + " items to update.");

            if (updateItems.size() !=0){
                log.info("Sending email...");
                // email impliment here
                log.info("Email sent.");
            }else{
                log.info("No items to update.");
            }

            log.info("Healthcheck finished.");
        }
    }
}