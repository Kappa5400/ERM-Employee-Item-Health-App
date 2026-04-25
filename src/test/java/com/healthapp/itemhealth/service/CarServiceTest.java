package com.healthapp.itemhealth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.healthapp.itemhealth.mapper.CarMapper;
import com.healthapp.itemhealth.model.Car;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

  @Mock private CarMapper carMapper;

  @InjectMocks private CarService carService;

  @Test
  void getById_Positive_ReturnsCar() {
    Car mockCar = new Car();
    mockCar.setCarId(1L);
    when(carMapper.findById(1L)).thenReturn(mockCar);

    Car result = carService.getById(1L);

    assertNotNull(result);
    assertEquals(1L, result.getCarId());
    verify(carMapper, times(1)).findById(1L);
  }

  @Test
  void getById_Negative_NotFoundReturnsNull() {
    when(carMapper.findById(99L)).thenReturn(null);
    assertNull(carService.getById(99L));
  }

  @Test
  void getById_NullPath_ReturnsNull() {
    when(carMapper.findById(null)).thenReturn(null);
    assertNull(carService.getById(null));
  }

  @Test
  void getAll_Positive_ReturnsList() {
    when(carMapper.findAll()).thenReturn(List.of(new Car(), new Car()));
    List<Car> result = carService.getAll();
    assertEquals(2, result.size());
  }

  @Test
  void getInUse_Positive_ReturnsList() {
    when(carMapper.findInUse()).thenReturn(List.of(new Car()));
    List<Car> result = carService.getInUse();
    assertFalse(result.isEmpty());
    verify(carMapper).findInUse();
  }

  @Test
  void getToService_Negative_ReturnsEmptyList() {

    when(carMapper.findToService()).thenReturn(List.of());
    List<Car> result = carService.getToService();
    assertTrue(result.isEmpty());
  }

  @Test
  void getToRenewInsurance_Positive_ReturnsList() {
    when(carMapper.findToRenewInsurance()).thenReturn(List.of(new Car()));
    List<Car> result = carService.getToRenewInsurance();
    assertNotNull(result);
    verify(carMapper).findToRenewInsurance();
  }

  @Test
  void getByEmployeeId_Positive_ReturnsCar() {
    Car car = new Car();
    car.setEmployeeId(5L);
    when(carMapper.findByEmployeeId(5L)).thenReturn(car);

    Car result = carService.getByEmployeeId(5L);

    assertNotNull(result);
    assertEquals(5L, result.getEmployeeId());
  }

  @Test
  void getByEmployeeId_NullPath_ReturnsNull() {
    when(carMapper.findByEmployeeId(null)).thenReturn(null);
    assertNull(carService.getByEmployeeId(null));
  }

  @Test
  void create_Positive_InvokesMapper() {
    Car car = new Car();
    car.setEmployeeId(10L);

    carService.create(car);

    verify(carMapper, times(1)).insert(car);
  }

  @Test
  void update_Positive_InvokesMapper() {
    Car car = new Car();
    car.setCarId(1L);

    carService.update(car);

    verify(carMapper, times(1)).update(car);
  }

  @Test
  void delete_Positive_InvokesMapper() {
    carService.delete(1L);
    verify(carMapper, times(1)).delete(1L);
  }

  @Test
  void delete_NullPath_HandlesNullId() {
    carService.delete(null);
    verify(carMapper, times(1)).delete(null);
  }
}
