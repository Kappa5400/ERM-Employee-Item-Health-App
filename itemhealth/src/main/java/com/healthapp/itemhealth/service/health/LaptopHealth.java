package com.healthapp.itemhealth.service.health;

import java.time.LocalDate;
import org.springframework.stereotype.Component;
import com.healthapp.itemhealth.model.Laptop;
import com.healthapp.mapper.LaptopMapper;

@Component
public class LaptopHealth implements HealthCheck<Laptop>{

    LaptopMapper mapper;

    private static final int MAX_AGE = 5;
    private static final int MAX_OS_AGE = 1;

    @Override
    public boolean checkUpdate(Laptop item){
        return renewCheck(item) || osUpdateCheck(item);
    }

    @Override
    public void performUpdate(Laptop item) {
        if (renewCheck(item))   mapper.setRenew(item.getLaptop_id(), true);
        if (osUpdateCheck(item))    mapper.setOSUpdate(item.getLaptop_id(), true);
    }

    private boolean renewCheck(Laptop item) {
        return item.getLaptopYear() <= LocalDate.now().getYear() - MAX_AGE;
    }

    private boolean osUpdateCheck(Laptop item){
        return item.getlastOSUpdate().getYear() <= LocalDate.now().getYear() - MAX_OS_AGE;
    }
    
}
