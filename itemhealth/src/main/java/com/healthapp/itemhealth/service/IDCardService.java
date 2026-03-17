package com.healthapp.itemhealth.service;

import com.healthapp.itemhealth.mapper.IDCardMapper;
import com.healthapp.itemhealth.model.IDCard;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class IDCardService {

  private final IDCardMapper idCardMapper;

  public IDCardService(IDCardMapper idCardMapper) {
    this.idCardMapper = idCardMapper;
  }

  public IDCard getById(Long idCardId) {
    return idCardMapper.findById(idCardId);
  }

  public List<IDCard> getAll() {
    return idCardMapper.findAll();
  }

  public IDCard getByEmployeeId(Long employeeId) {
    return idCardMapper.getByEmployeeId(employeeId);
  }

  public List<IDCard> getInUse() {
    return idCardMapper.findInUse();
  }

  public List<IDCard> getToRenew() {
    return idCardMapper.findToRenew();
  }

  public void create(IDCard idCard) {
    idCardMapper.insert(idCard);
  }

  public void update(IDCard idCard) {
    idCardMapper.update(idCard);
  }

  public void delete(Long idCardId) {
    idCardMapper.delete(idCardId);
  }
}
