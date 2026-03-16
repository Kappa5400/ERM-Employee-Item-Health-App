package com.healthapp.itemhealth.service.health;

import java.util.List;
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

    private final EmployeeService employeeService;  
    private final LaptopService laptopService;      
    private final CarService carService;            
    private final IdCardService idCardService;      
    private final LaptopHealth laptopHealth;       
    private final CarHealth carHealth;              
    private final IdCardHealth idCardHealth;        

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
        List<Employee> allEmployees = employeeService.getAll();   

        for (Employee employee : allEmployees) {
            Long employeeId = employee.getEmployeeId();             

            Laptop laptop = laptopService.getByEmployeeId(employeeId);
            Car car = carService.getByEmployeeId(employeeId);
            IdCard idCard = idCardService.getByEmployeeId(employeeId); 

            if (laptop != null) {
                if (laptopHealth.checkUpdate(laptop)) {
                    laptopHealth.performUpdate(laptop);
                }
            }

            if (car != null) {
                if (carHealth.checkUpdate(car)) {
                    carHealth.performUpdate(car);
                }
            }

            if (idCard != null) {
                if (idCardHealth.checkUpdate(idCard)) {
                    idCardHealth.performUpdate(idCard);
                }
            }
        }
    }
}