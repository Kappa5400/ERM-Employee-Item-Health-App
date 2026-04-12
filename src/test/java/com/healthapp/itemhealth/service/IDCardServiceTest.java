package com.healthapp.itemhealth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.healthapp.itemhealth.mapper.IDCardMapper;
import com.healthapp.itemhealth.model.IDCard;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class IDCardServiceTest {

  @Mock private IDCardMapper idCardMapper;

  @InjectMocks private IDCardService idCardService;

  @Test
  void getById_Positive_ReturnsIDCard() {
    IDCard mockCard = new IDCard();
    mockCard.setIdCardId(1L);
    when(idCardMapper.findById(1L)).thenReturn(mockCard);

    IDCard result = idCardService.getById(1L);

    assertNotNull(result);
    assertEquals(1L, result.getIdCardId());
    verify(idCardMapper, times(1)).findById(1L);
  }

  @Test
  void getById_Negative_NotFoundReturnsNull() {
    when(idCardMapper.findById(99L)).thenReturn(null);
    assertNull(idCardService.getById(99L));
  }

  @Test
  void getById_NullPath_ReturnsNull() {
    when(idCardMapper.findById(null)).thenReturn(null);
    assertNull(idCardService.getById(null));
  }

  // --- LIST RETRIEVAL (ALL, IN USE, RENEW) ---

  @Test
  void getAll_Positive_ReturnsList() {
    when(idCardMapper.findAll()).thenReturn(List.of(new IDCard(), new IDCard()));
    List<IDCard> result = idCardService.getAll();
    assertEquals(2, result.size());
  }

  @Test
  void getInUse_Positive_ReturnsList() {
    when(idCardMapper.findInUse()).thenReturn(List.of(new IDCard()));
    List<IDCard> result = idCardService.getInUse();
    assertFalse(result.isEmpty());
    verify(idCardMapper).findInUse();
  }

  @Test
  void getToRenew_Negative_ReturnsEmptyList() {
    when(idCardMapper.findToRenew()).thenReturn(List.of());
    List<IDCard> result = idCardService.getToRenew();
    assertTrue(result.isEmpty());
  }

  // --- EMPLOYEE RELATIONSHIP ---

  @Test
  void getByEmployeeId_Positive_ReturnsIDCard() {
    IDCard card = new IDCard();
    card.setEmployeeId(5L);
    when(idCardMapper.getByEmployeeId(5L)).thenReturn(card);

    IDCard result = idCardService.getByEmployeeId(5L);

    assertNotNull(result);
    assertEquals(5L, result.getEmployeeId());
  }

  @Test
  void getByEmployeeId_NullPath_ReturnsNull() {
    when(idCardMapper.getByEmployeeId(null)).thenReturn(null);
    assertNull(idCardService.getByEmployeeId(null));
  }

  @Test
  void create_Positive_InvokesMapper() {
    IDCard card = new IDCard();
    card.setEmployeeId(10L);

    idCardService.create(card);

    verify(idCardMapper, times(1)).insert(card);
  }

  @Test
  void update_Positive_InvokesMapper() {
    IDCard card = new IDCard();
    card.setIdCardId(1L);

    idCardService.update(card);

    verify(idCardMapper, times(1)).update(card);
  }

  @Test
  void delete_Positive_InvokesMapper() {
    idCardService.delete(1L);
    verify(idCardMapper, times(1)).delete(1L);
  }

  @Test
  void delete_NullPath_HandlesNullId() {
    idCardService.delete(null);
    verify(idCardMapper, times(1)).delete(null);
  }
}
