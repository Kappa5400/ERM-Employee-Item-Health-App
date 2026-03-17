package com.healthapp.itemhealth.service.health;

import com.healthapp.itemhealth.mapper.CarMapper;
import com.healthapp.itemhealth.model.Car;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class CarHealth implements HealthCheck<Car> {

  private static final int CAR_MAX_AGE = 10;
  private static final int CAR_SERVICE_FREQUENCY = 3;
  CarMapper carMapper;

  @Override
  public boolean checkUpdate(Car item) {
    return carReplaceCheck(item) || carInsuranceCheck(item) || carServiceCheck(item);
  }

  @Override
  public void performUpdate(Car item) {
    if (carReplaceCheck(item)) carMapper.setReplace(item.getCarId(), true);
    if (carInsuranceCheck(item)) carMapper.setInsurance(item.getCarId(), true);
    if (carServiceCheck(item)) carMapper.setService(item.getCarId(), true);
  }

  private boolean carReplaceCheck(Car item) {
    return item.getCarYear() <= LocalDate.now().getYear() - CAR_MAX_AGE;
  }

  private boolean carInsuranceCheck(Car item) {
    return item.getInsuranceExpireDate().isBefore(LocalDate.now());
  }

  private boolean carServiceCheck(Car item) {
    return item.getLastInsuranceRenewal()
        .isBefore(LocalDate.now().minusMonths(CAR_SERVICE_FREQUENCY));
  }
}
