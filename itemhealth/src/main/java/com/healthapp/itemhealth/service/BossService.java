package com.healthapp.itemhealth.service;

import com.healthapp.itemhealth.mapper.BossMapper;
import com.healthapp.itemhealth.model.Boss;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BossService {

  private final BossMapper bossMapper;

  public BossService(BossMapper bossMapper) {
    this.bossMapper = bossMapper;
  }

  public Boss findById(Long bossId) {
    return bossMapper.findById(bossId);
  }

  public List<Boss> findAll() {
    return bossMapper.findAll();
  }

  public List<Long> findSubordinateIds(Long bossId) {
    return bossMapper.findSubordinateIds(bossId);
  }

  public void insert(Boss boss) {
    bossMapper.insert(boss);
  }

  public void insertSubordinate(Long bossId, long subordinateId) {
    bossMapper.insertSubordinate(bossId, subordinateId);
  }

  public void update(Boss boss) {
    bossMapper.update(boss);
  }

  public void delete(Long bossId) {
    bossMapper.delete(bossId);
  }

  public void deleteSubordinate(Long bossId, Long subordinateId) {
    bossMapper.deleteSubordinate(bossId, subordinateId);
  }

  public void deleteAllSubordinates(Long bossId) {
    bossMapper.deleteAllSubordinates(bossId);
  }
}
