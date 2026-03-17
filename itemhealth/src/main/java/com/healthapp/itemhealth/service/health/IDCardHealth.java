package com.healthapp.itemhealth.service.health;

import java.time.LocalDate;
import org.springframework.stereotype.Component;
import com.healthapp.itemhealth.model.IDCard;
import com.healthapp.itemhealth.mapper.IDCardMapper;

@Component
public class IDCardHealth implements HealthCheck<ICard> {

    IDCardMapper idMapper;

    @Override
    public boolean checkUpdate(IDCard item){
    return renewCheck(item);    
    }

    @Override
    public void performUpdate(IDCard item) {
        if (renewCheck(item)) idMapper.setRenew(item.getId_card_id(), true);
    }

    

    private boolean renewCheck(IDCard item){
        return item.getNeedToRenewDate().isBefore(LocalDate.now());
    }


}