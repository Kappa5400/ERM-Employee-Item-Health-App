package com.healthapp.itemhealth.service.health;

import java.time.LocalDate;
import org.springframework.stereotype.Component;
import com.healthapp.itemhealth.model.Car;

@Component
public class CarHealth implements HealthCheck<Car>{

    private static final int CAR_MAX_AGE = 10;
    private static final int CAR_INSURANCE_FREQUENCY = 1;
    private static final int CAR_SERVICE_FREQUENCY = 3;

    @Override
    public boolean checkUpdate(Car item){
        return carRenewCheck(item) || carInsuranceCheck(item) || carServiceCheck(item);
    }

    @Override
    public void performUpdate(Car item){
        if (carInsuranceCheck(item))item.setToRenewInsurance(true);
        if (carRenewCheck(item))item.setToReplace(true);
        if (carServiceCheck(item))item.setToService(true);
    }

    private boolean car_renew_check(Car item){
        return item.getCar_Year() <= LocalDate.now().getYear() - CAR_MAX_AGE;
    }

    private boolean car_insurance_check(Car item){
        return item.getLast_Insurance_Renewal().isBefore(LocalDate.now()); 
    }

    private boolean car_service_check(Car item){
        return item.getLastServiced().isBefore(LocalDate.now().minusMonths(CAR_SERVICE_FREQUENCY)); 
    }

}