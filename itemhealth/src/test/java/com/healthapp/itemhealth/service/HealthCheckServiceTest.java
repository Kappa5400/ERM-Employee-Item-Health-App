package com.healthapp.itemhealth.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.healthapp.itemhealth.model.Car;
import com.healthapp.itemhealth.model.Employee;
import com.healthapp.itemhealth.model.IDCard;
import com.healthapp.itemhealth.model.Laptop;
import com.healthapp.itemhealth.service.health.CarHealth;
import com.healthapp.itemhealth.service.health.IDCardHealth;
import com.healthapp.itemhealth.service.health.LaptopHealth;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HealthCheckServiceTest {

  @Mock private EmployeeService employeeService;
  @Mock private LaptopService laptopService;
  @Mock private CarService carService;
  @Mock private IDCardService idCardService;
  @Mock private LaptopHealth laptopHealth;
  @Mock private CarHealth carHealth;
  @Mock private IDCardHealth idCardHealth;

  @InjectMocks private HealthCheckService healthCheckService;

  @Test
  void runHealthCheck_Positive_UpdatesAllEquipmentWhenNeeded() {
    // Arrange
    Employee emp = new Employee();
    emp.setEmployeeId(1L);
    when(employeeService.findAllEmployees()).thenReturn(List.of(emp));

    Laptop laptop = new Laptop();
    Car car = new Car();
    IDCard idCard = new IDCard();

    when(laptopService.getByEmployeeId(1L)).thenReturn(laptop);
    when(carService.getByEmployeeId(1L)).thenReturn(car);
    when(idCardService.getByEmployeeId(1L)).thenReturn(idCard);

    // Set all to true to trigger updates
    when(laptopHealth.checkUpdate(laptop)).thenReturn(true);
    when(carHealth.checkUpdate(car)).thenReturn(true);
    when(idCardHealth.checkUpdate(idCard)).thenReturn(true);

    // Act
    healthCheckService.runHealthCheck();

    // Assert: Verify that performUpdate was called for each device
    verify(laptopHealth, times(1)).performUpdate(laptop);
    verify(carHealth, times(1)).performUpdate(car);
    verify(idCardHealth, times(1)).performUpdate(idCard);
  }

  @Test
  void runHealthCheck_Negative_NoUpdatesIfHealthy() {
    // Arrange
    Employee emp = new Employee();
    emp.setEmployeeId(1L);
    when(employeeService.findAllEmployees()).thenReturn(List.of(emp));

    Laptop laptop = new Laptop();
    when(laptopService.getByEmployeeId(1L)).thenReturn(laptop);
    when(laptopHealth.checkUpdate(laptop)).thenReturn(false); // No update needed

    // Act
    healthCheckService.runHealthCheck();

    // Assert
    verify(laptopHealth, never()).performUpdate(any());
  }

  @Test
  void runHealthCheck_NullPath_HandlesMissingEquipment() {
    // Arrange: Employee exists but has no laptop (returns null)
    Employee emp = new Employee();
    emp.setEmployeeId(1L);
    when(employeeService.findAllEmployees()).thenReturn(List.of(emp));
    when(laptopService.getByEmployeeId(1L)).thenReturn(null);

    // Act
    healthCheckService.runHealthCheck();

    // Assert: Should not even check health if the object is null
    verify(laptopHealth, never()).checkUpdate(any());
  }
}
