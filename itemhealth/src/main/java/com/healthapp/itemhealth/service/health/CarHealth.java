package com.healthapp.itemhealth.service.health;

import java.time.LocalDate;
import org.springframework.stereotype.Component;
import com.healthapp.itemhealth.model.Car;

@Component
public class CarHealth implements HealthCheck<Car> {

    private static final int CAR_MAX_AGE = 10;
    private static final int CAR_INSURANCE_FREQUENCY = 1;
    private static final int CAR_SERVICE_FREQUENCY = 3;

    @Override
    public boolean checkUpdate(Car item) {
        return carRenewCheck(item) || carInsuranceCheck(item) || carServiceCheck(item);
    }

    @Override
    public void performUpdate(Car item) {
        if (carRenewCheck(item))     item.setToRenew(true);
        if (carInsuranceCheck(item)) item.setToRenewInsurance(true);
        if (carServiceCheck(item))   item.setToService(true);
    }

    private boolean carRenewCheck(Car item) {                         
        return item.getCarYear() <= LocalDate.now().getYear() - CAR_MAX_AGE;  
    }

    private boolean carInsuranceCheck(Car item) {                     
        return item.getInsuranceExpireDate().isBefore(LocalDate.now()); 
    }

    private boolean carServiceCheck(Car item) {                        
        return item.getLastServiced().isBefore(
                LocalDate.now().minusMonths(CAR_SERVICE_FREQUENCY));
    }
}