package com.healthapp.itemhealth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.healthapp.itemhealth.mapper.BossMapper;
import com.healthapp.itemhealth.model.Boss;
import com.healthapp.itemhealth.model.Employee;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BossServiceTest {

  @Mock private BossMapper bossMapper;

  @InjectMocks private BossService bossService;

  @Test
  void findById_ReturnsbossId() {
    Boss mockBoss = new Boss();
    mockBoss.setBossId(10L);
    mockBoss.setEmployeeId(2L);

    when(bossMapper.findById(10L)).thenReturn(mockBoss);

    Boss result = bossService.findById(10L);

    assertNotNull(result);
    assertEquals(2L, result.getEmployeeId());

    verify(bossMapper, times(1)).findById(10L);
  }

  @Test
  void findById_noIdFoundReturnsNull() {
    Boss mockBoss = new Boss();
    mockBoss.setBossId(10L);
    mockBoss.setEmployeeId(2L);

    when(bossMapper.findById(5L)).thenReturn(null);

    Boss result = bossService.findById(5L);

    assertNull(result);

    verify(bossMapper, times(1)).findById(5L);
  }

  @Test
  void findById_nullParamReturnsNull() {

    when(bossMapper.findById(null)).thenReturn(null);

    Boss result = bossService.findById(null);

    assertNull(result);
    verify(bossMapper, times(1)).findById(null);
  }

  @Test
  public void findAll_Returnslistofbosses() {

    Boss mockBoss1 = new Boss();
    mockBoss1.setBossId(1L);
    mockBoss1.setEmployeeId(1L);

    Boss mockBoss2 = new Boss();
    mockBoss2.setBossId(2L);
    mockBoss2.setEmployeeId(2L);

    List<Boss> expect = List.of(mockBoss1, mockBoss2);

    when(bossMapper.findAll()).thenReturn(expect);

    List<Boss> res = bossService.findAll();

    assertNotNull(res);
    assertEquals(2, res.size());
    assertEquals(expect, res);
    verify(bossMapper, times(1)).findAll();
  }

  @Test
  public void findAll_empty() {

    when(bossMapper.findAll()).thenReturn(null);
    List<Boss> res = bossService.findAll();
    assertNull(res);
    verify(bossMapper, times(1)).findAll();
  }

  @Test
  public void findSubordinateIds_returnsSubId() {
    Boss mockBoss = new Boss();
    mockBoss.setBossId(10L);
    mockBoss.setEmployeeId(1L);

    Employee mockEmployee = new Employee();
    mockEmployee.setBossUserId(1L);
    mockEmployee.setEmployeeId(2L);

    Employee mockEmployee2 = new Employee();
    mockEmployee2.setBossUserId(1L);
    mockEmployee2.setEmployeeId(3L);

    List<Long> expect = List.of(mockEmployee.getEmployeeId(), mockEmployee2.getEmployeeId());

    when(bossMapper.findSubordinateIds(10L)).thenReturn(expect);

    List<Long> res = bossService.findSubordinateIds(mockBoss.getBossId());

    assertNotNull(res);
    assertEquals(2, res.size());
    assertEquals(expect, res);
    verify(bossMapper, times(1)).findSubordinateIds(10L);
  }

  @Test
  public void insert_insertBoss() {
    Boss mockBoss = new Boss();
    mockBoss.setBossId(10L);
    mockBoss.setEmployeeId(1L);

    bossService.insert(mockBoss);

    verify(bossMapper, times(1)).insert(mockBoss);
  }

  @Test
  public void update_updateBoss() {

    Boss mockBoss = new Boss();
    mockBoss.setBossId(10L);
    mockBoss.setEmployeeId(1L);

    bossService.update(mockBoss);

    verify(bossMapper, times(1)).update(mockBoss);
  }

  @Test
  public void deleteAllSubordinates_deleates() {
    bossService.deleteAllSubordinates(10L);

    verify(bossMapper, times(1)).deleteAllSubordinates(10L);
  }

  @Test
  void findSubordinateIds_noSubordinatesReturnsEmptyList() {
    when(bossMapper.findSubordinateIds(10L)).thenReturn(List.of());

    List<Long> res = bossService.findSubordinateIds(10L);

    assertNotNull(res);
    assertTrue(res.isEmpty());
    verify(bossMapper, times(1)).findSubordinateIds(10L);
  }

  @Test
  void findSubordinateIds_nullIdReturnsNull() {

    when(bossMapper.findSubordinateIds(null)).thenReturn(null);

    List<Long> res = bossService.findSubordinateIds(null);

    assertNull(res);
    verify(bossMapper, times(1)).findSubordinateIds(null);
  }

  @Test
  public void insertSubordinate_linksIds() {

    bossService.insertSubordinate(10L, 5L);

    verify(bossMapper, times(1)).insertSubordinate(10L, 5L);
  }

  @Test
  public void deleteSubordinate_removesLink() {

    bossService.deleteSubordinate(10L, 5L);

    verify(bossMapper, times(1)).deleteSubordinate(10L, 5L);
  }

  @Test
  public void delete_removesBoss() {

    Boss mockBoss = new Boss();
    mockBoss.setBossId(10L);
    mockBoss.setEmployeeId(1L);

    bossService.delete(10L);

    verify(bossMapper, times(1)).delete(10L);
  }
}
