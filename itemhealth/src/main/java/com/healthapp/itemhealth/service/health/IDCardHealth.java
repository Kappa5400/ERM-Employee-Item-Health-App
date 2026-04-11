package com.healthapp.itemhealth.service.health;

import com.healthapp.itemhealth.mapper.IDCardMapper;
import com.healthapp.itemhealth.model.IDCard;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IDCardHealth implements HealthCheck<IDCard> {

  private final IDCardMapper idMapper;

  @Override
  public boolean checkUpdate(IDCard item) {
    return renewCheck(item);
  }

  @Override
  public void performUpdate(IDCard item) {
    if (renewCheck(item)) idMapper.setRenew(item.getIdCardId(), true);
  }

  private boolean renewCheck(IDCard item) {
    return item.getNeedToRenewDate().isBefore(LocalDate.now());
  }
}
