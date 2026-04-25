package com.healthapp.itemhealth.service.health;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.healthapp.itemhealth.mapper.IDCardMapper;
import com.healthapp.itemhealth.model.IDCard;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class IDCardHealthTest {

  @Mock private IDCardMapper idMapper;

  @InjectMocks private IDCardHealth idCardHealth;

  private IDCard idCard;

  @BeforeEach
  void setUp() {
    idCard = new IDCard();
    idCard.setIdCardId(500L);

    idCard.setNeedToRenewDate(LocalDate.now().plusDays(1));
  }

  @Test
  void checkUpdate_Positive_ReturnsTrueWhenExpired() {
    idCard.setNeedToRenewDate(LocalDate.now().minusDays(1));
    assertTrue(idCardHealth.checkUpdate(idCard));
  }

  @Test
  void checkUpdate_Negative_ReturnsFalseWhenValid() {
    assertFalse(idCardHealth.checkUpdate(idCard));
  }

  @Test
  void performUpdate_InvokesMapperWhenNeeded() {
    idCard.setNeedToRenewDate(LocalDate.now().minusDays(5));

    idCardHealth.performUpdate(idCard);

    verify(idMapper, times(1)).setRenew(500L, true);
  }

  @Test
  void performUpdate_DoesNotInvokeMapperWhenHealthy() {
    idCardHealth.performUpdate(idCard);
    verify(idMapper, never()).setRenew(anyLong(), anyBoolean());
  }
}
