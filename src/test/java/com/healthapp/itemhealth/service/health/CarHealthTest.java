package com.healthapp.itemhealth.service.health;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.healthapp.itemhealth.mapper.CarMapper;
import com.healthapp.itemhealth.model.Car;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CarHealthTest {

  @Mock private CarMapper carMapper;

  @InjectMocks private CarHealth carHealth;

  private Car car;

  @BeforeEach
  void setUp() {
    car = new Car();
    car.setCarId(1L);
    // Default "Healthy" state
    car.setCarYear(LocalDate.now().getYear() - 2);
    car.setInsuranceExpireDate(LocalDate.now().plusMonths(6));
    car.setLastInsuranceRenewal(LocalDate.now().minusMonths(1));
  }

  // --- CHECK UPDATE TESTS (LOGIC) ---

  @Test
  void checkUpdate_Positive_ReturnsTrueWhenTooOld() {
    // Car is exactly 10 years old
    car.setCarYear(LocalDate.now().getYear() - 10);
    assertTrue(carHealth.checkUpdate(car));
  }

  @Test
  void checkUpdate_Positive_ReturnsTrueWhenInsuranceExpired() {
    car.setInsuranceExpireDate(LocalDate.now().minusDays(1));
    assertTrue(carHealth.checkUpdate(car));
  }

  @Test
  void checkUpdate_Positive_ReturnsTrueWhenServiceOverdue() {
    // Service frequency is 3 months
    car.setLastInsuranceRenewal(LocalDate.now().minusMonths(4));
    assertTrue(carHealth.checkUpdate(car));
  }

  @Test
  void checkUpdate_Negative_ReturnsFalseWhenAllHealthy() {
    assertFalse(carHealth.checkUpdate(car));
  }

  // --- PERFORM UPDATE TESTS (MAPPER INTERACTIONS) ---

  @Test
  void performUpdate_CallsMapperForReplacement() {
    car.setCarYear(LocalDate.now().getYear() - 11);

    carHealth.performUpdate(car);

    verify(carMapper, times(1)).setReplace(1L, true);
    // Ensure other updates weren't called if their conditions weren't met
    verify(carMapper, never()).setInsurance(anyLong(), anyBoolean());
  }

  @Test
  void performUpdate_CallsMultipleUpdatesWhenNeeded() {
    // Both old and insurance expired
    car.setCarYear(LocalDate.now().getYear() - 15);
    car.setInsuranceExpireDate(LocalDate.now().minusYears(1));

    carHealth.performUpdate(car);

    verify(carMapper).setReplace(1L, true);
    verify(carMapper).setInsurance(1L, true);
  }

  // --- NULL PATHS ---

  @Test
  void checkUpdate_NullPath_ThrowsException() {
    // Testing how the component handles a null car object
    assertThrows(
        NullPointerException.class,
        () -> {
          carHealth.checkUpdate(null);
        });
  }
}
