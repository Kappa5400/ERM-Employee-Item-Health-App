package com.healthapp.itemhealth.service.health;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.healthapp.itemhealth.mapper.LaptopMapper;
import com.healthapp.itemhealth.model.Laptop;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LaptopHealthTest {

  @Mock private LaptopMapper laptopMapper;

  @InjectMocks private LaptopHealth laptopHealth;

  private Laptop laptop;

  @BeforeEach
  void setUp() {
    laptop = new Laptop();
    laptop.setLaptopId(101L);
    laptop.setLaptopYear(LocalDate.now().getYear() - 1);
    laptop.setLastOSUpdate(LocalDate.now());


    laptop.setOsVersion(11);
  }

  @Test
  void checkUpdate_Positive_ReturnsTrueWhenOSTooOld() {
 
    laptop.setLastOSUpdate(LocalDate.now().minusYears(1));
    assertTrue(laptopHealth.checkUpdate(laptop));
  }

  @Test
  void checkUpdate_Positive_ReturnsTrueWhenHardwareTooOld() {

    laptop.setLaptopYear(LocalDate.now().getYear() - 5);
    assertTrue(laptopHealth.checkUpdate(laptop));
  }

  @Test
  void checkUpdate_Negative_ReturnsFalseWhenHealthy() {

    assertFalse(laptopHealth.checkUpdate(laptop));
  }

  @Test
  void performUpdate_InvokesMapperWithIntVersion() {

    laptop.setLastOSUpdate(LocalDate.now().minusYears(2));

    laptopHealth.performUpdate(laptop);

    verify(laptopMapper, times(1)).setOSUpdate(101L, true);
  }
}
