package com.healthapp.itemhealth.service.health;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import com.healthapp.itemhealth.mapper.LaptopMapper;
import com.healthapp.itemhealth.model.Laptop;

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

    // Updated: osVersion is now an int
    laptop.setOsVersion(11);
  }

  @Test
  void checkUpdate_Positive_ReturnsTrueWhenOSTooOld() {
    // Logic: lastOSUpdate was 1+ year ago (MAX_OS_AGE = 1)
    laptop.setLastOSUpdate(LocalDate.now().minusYears(1));
    assertTrue(laptopHealth.checkUpdate(laptop));
  }

  @Test
  void checkUpdate_Positive_ReturnsTrueWhenHardwareTooOld() {
    // Logic: Laptop is 5+ years old (MAX_AGE = 5)
    laptop.setLaptopYear(LocalDate.now().getYear() - 5);
    assertTrue(laptopHealth.checkUpdate(laptop));
  }

  @Test
  void checkUpdate_Negative_ReturnsFalseWhenHealthy() {
    // Current OS update and relatively new hardware
    assertFalse(laptopHealth.checkUpdate(laptop));
  }

  @Test
  void performUpdate_InvokesMapperWithIntVersion() {
    // Triggering an OS update
    laptop.setLastOSUpdate(LocalDate.now().minusYears(2));

    laptopHealth.performUpdate(laptop);

    // Verifying the mapper call
    verify(laptopMapper, times(1)).setOSUpdate(101L, true);
  }
}
