package com.healthapp.itemhealth.service.health;

import com.healthapp.itemhealth.mapper.LaptopMapper;
import com.healthapp.itemhealth.model.Laptop;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LaptopHealth implements HealthCheck<Laptop> {

  private final LaptopMapper laptopMapper;

  private static final int MAX_AGE = 5;
  private static final int MAX_OS_AGE = 1;

  @Override
  public boolean checkUpdate(Laptop item) {
    return renewCheck(item) || osUpdateCheck(item);
  }

  @Override
  public void performUpdate(Laptop item) {
    if (renewCheck(item)) laptopMapper.setRenew(item.getLaptopId(), true);
    if (osUpdateCheck(item)) laptopMapper.setOSUpdate(item.getLaptopId(), true);
  }

  private boolean renewCheck(Laptop item) {
    return item.getLaptopYear() <= LocalDate.now().getYear() - MAX_AGE;
  }

  private boolean osUpdateCheck(Laptop item) {
    return item.getLastOSUpdate().getYear() <= LocalDate.now().getYear() - MAX_OS_AGE;
  }
}
