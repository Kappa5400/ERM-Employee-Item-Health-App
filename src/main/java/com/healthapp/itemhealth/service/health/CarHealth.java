package com.healthapp.itemhealth.service.health;

import com.healthapp.itemhealth.mapper.CarMapper;
import com.healthapp.itemhealth.model.Car;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// For all items how each checks if the
// item needs to be 'renewed' or 'updated'
// the logic changes, as such we make the
// health check function polymorphic. For the car class,
// cars that are over 10 years old need to be renewed, and
// require servicing every 3 months. If either field
// is over the limit, a boolean flag is flipped
// and it will trigger the mail logic to detect the item needs to be updated
// and will include it in the automatic email that goes to every boss employee.

@Component
@RequiredArgsConstructor
public class CarHealth implements HealthCheck<Car> {

  private static final int CAR_MAX_AGE = 10;
  private static final int CAR_SERVICE_FREQUENCY = 3;
  private final CarMapper carMapper;

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
