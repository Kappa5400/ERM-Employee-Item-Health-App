package com.healthapp.itemhealth.service.health;

import java.time.LocalDate;
import org.springframework.stereotype.Component;
import com.healthapp.itemhealth.model.IDCard;

@Component
public class IDCardHealth implements HealthCheck<ICard> {

    @Override
    public boolean checkUpdate(IDCard item){
    return renewCheck(item);    
    }

    @Override
    public void performUpdate(IDCard item) {
        if (renewCheck(item)) item.setToRenew(true);
    }

    

    private boolean renewCheck(IDCard item){
        return item.getNeedToRenewDate().isBefore(LocalDate.now());
    }


}