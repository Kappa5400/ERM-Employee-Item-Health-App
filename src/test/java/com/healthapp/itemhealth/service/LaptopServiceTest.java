package com.healthapp.itemhealth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.healthapp.itemhealth.mapper.LaptopMapper;
import com.healthapp.itemhealth.model.Laptop;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LaptopServiceTest {

  @Mock private LaptopMapper laptopMapper;

  @InjectMocks private LaptopService laptopService;

  @Test
  void getById_Positive_ReturnsLaptop() {
    Laptop mockLaptop = new Laptop();
    mockLaptop.setLaptopId(1L);
    when(laptopMapper.findById(1L)).thenReturn(mockLaptop);

    Laptop result = laptopService.getById(1L);

    assertNotNull(result);
    assertEquals(1L, result.getLaptopId());
    verify(laptopMapper, times(1)).findById(1L);
  }

  @Test
  void getById_Negative_NotFoundReturnsNull() {
    when(laptopMapper.findById(99L)).thenReturn(null);
    assertNull(laptopService.getById(99L));
  }

  @Test
  void getById_NullPath_ReturnsNull() {
    when(laptopMapper.findById(null)).thenReturn(null);
    assertNull(laptopService.getById(null));
  }

  @Test
  void getAll_Positive_ReturnsList() {
    when(laptopMapper.findAll()).thenReturn(List.of(new Laptop(), new Laptop()));
    List<Laptop> result = laptopService.getAll();
    assertEquals(2, result.size());
  }

  @Test
  void getInUse_Positive_ReturnsFilteredList() {
    when(laptopMapper.findInUse()).thenReturn(List.of(new Laptop()));
    List<Laptop> result = laptopService.getInUse();
    assertFalse(result.isEmpty());
    verify(laptopMapper).findInUse();
  }

  @Test
  void getNeedToUpdate_Negative_ReturnsEmptyList() {

    when(laptopMapper.findNeedToUpdate()).thenReturn(List.of());
    List<Laptop> result = laptopService.getNeedToUpdate();
    assertTrue(result.isEmpty());
  }

  @Test
  void getToRenew_Positive_ReturnsList() {
    when(laptopMapper.findToRenew()).thenReturn(List.of(new Laptop()));
    List<Laptop> result = laptopService.getToRenew();
    assertNotNull(result);
    verify(laptopMapper).findToRenew();
  }

  @Test
  void getByEmployeeId_Positive_ReturnsLaptop() {
    Laptop laptop = new Laptop();
    laptop.setEmployeeId(50L);
    when(laptopMapper.findByEmployeeId(50L)).thenReturn(laptop);

    Laptop result = laptopService.getByEmployeeId(50L);

    assertEquals(50L, result.getEmployeeId());
  }

  @Test
  void create_Positive_InvokesMapper() {
    Laptop laptop = new Laptop();
    laptop.setEmployeeId(5L);

    laptopService.create(laptop);

    verify(laptopMapper, times(1)).insert(laptop);
  }

  @Test
  void update_Positive_InvokesMapper() {
    Laptop laptop = new Laptop();
    laptop.setLaptopId(10L);

    laptopService.update(laptop);

    verify(laptopMapper, times(1)).update(laptop);
  }

  @Test
  void delete_Positive_InvokesMapper() {
    laptopService.delete(1L);
    verify(laptopMapper, times(1)).delete(1L);
  }

  @Test
  void getAll_Negative_ReturnsEmptyListWhenNoLaptops() {

    when(laptopMapper.findAll()).thenReturn(List.of());

    List<Laptop> result = laptopService.getAll();

    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(laptopMapper, times(1)).findAll();
  }

  @Test
  void getInUse_Negative_ReturnsEmptyListWhenNoneInUse() {
    when(laptopMapper.findInUse()).thenReturn(List.of());

    List<Laptop> result = laptopService.getInUse();

    assertTrue(result.isEmpty());
  }

  @Test
  void getByEmployeeId_Negative_ReturnsNullWhenEmployeeHasNoLaptop() {

    when(laptopMapper.findByEmployeeId(101L)).thenReturn(null);

    Laptop result = laptopService.getByEmployeeId(101L);

    assertNull(result);
    verify(laptopMapper, times(1)).findByEmployeeId(101L);
  }

  @Test
  void getByEmployeeId_NullPath_HandlesNullEmployeeId() {

    when(laptopMapper.findByEmployeeId(null)).thenReturn(null);

    Laptop result = laptopService.getByEmployeeId(null);

    assertNull(result);
    verify(laptopMapper, times(1)).findByEmployeeId(null);
  }

  @Test
  void delete_NullPath_HandlesNullId() {

    laptopService.delete(null);

    verify(laptopMapper, times(1)).delete(null);
  }
}
