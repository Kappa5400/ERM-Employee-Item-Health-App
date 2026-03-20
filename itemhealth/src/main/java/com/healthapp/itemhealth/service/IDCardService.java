package com.healthapp.itemhealth.service;

import com.healthapp.itemhealth.mapper.IDCardMapper;
import com.healthapp.itemhealth.model.IDCard;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class IDCardService {

  private static final Logger log = LoggerFactory.getLogger(IDCardService.class);

  private final IDCardMapper idCardMapper;

  public IDCardService(IDCardMapper idCardMapper) {
    this.idCardMapper = idCardMapper;
  }

  public IDCard getById(Long idCardId) {
    log.info("Fetching ID card with ID: {}", idCardId);
    return idCardMapper.findById(idCardId);
  }

  public List<IDCard> getAll() {
    log.info("Fetching all ID cards");
    return idCardMapper.findAll();
  }

  public IDCard getByEmployeeId(Long employeeId) {
    log.info("Fetching ID card for employee ID: {}", employeeId);
    return idCardMapper.getByEmployeeId(employeeId);
  }

  public List<IDCard> getInUse() {
    log.info("Fetching ID cards currently in use");
    return idCardMapper.findInUse();
  }

  public List<IDCard> getToRenew() {
    log.info("Fetching ID cards requiring renewal");
    return idCardMapper.findToRenew();
  }

  @PreAuthorize("HasRole'BOSS")
  public void create(IDCard idCard) {
    log.info("Creating new ID card for employee ID: {}", idCard.getEmployeeId());
    idCardMapper.insert(idCard);
  }

  @PreAuthorize("HasRole'BOSS")
  public void update(IDCard idCard) {
    log.info("Updating ID card with ID: {}", idCard.getIdCardId());
    idCardMapper.update(idCard);
  }

  @PreAuthorize("HasRole'BOSS")
  public void delete(Long idCardId) {
    log.info("Deleting ID card with ID: {}", idCardId);
    idCardMapper.delete(idCardId);
  }
}