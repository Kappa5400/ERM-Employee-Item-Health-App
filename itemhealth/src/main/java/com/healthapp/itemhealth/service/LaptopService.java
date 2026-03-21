package com.healthapp.itemhealth.service;

import com.healthapp.itemhealth.mapper.LaptopMapper;
import com.healthapp.itemhealth.model.Laptop;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class LaptopService {

  private static final Logger log = LoggerFactory.getLogger(LaptopService.class);

  private final LaptopMapper laptopMapper;

  public LaptopService(LaptopMapper laptopMapper) {
    this.laptopMapper = laptopMapper;
  }

  public Laptop getById(Long laptopId) {
    log.info("Fetching laptop with ID: {}", laptopId);
    return laptopMapper.findById(laptopId);
  }

  public List<Laptop> getAll() {
    log.info("Fetching all laptops");
    return laptopMapper.findAll();
  }

  public Laptop getByEmployeeId(Long employeeId) {
    log.info("Fetching laptop for employee ID: {}", employeeId);
    return laptopMapper.findByEmployeeId(employeeId);
  }

  public List<Laptop> getNeedToUpdate() {
    log.info("Fetching laptops requiring software updates");
    return laptopMapper.findNeedToUpdate();
  }

  public List<Laptop> getInUse() {
    log.info("Fetching all laptops currently in use");
    return laptopMapper.findInUse();
  }

  public List<Laptop> getToRenew() {
    log.info("Fetching laptops requiring warranty or insurance renewal");
    return laptopMapper.findToRenew();
  }

  @PreAuthorize("HasRole'BOSS")
  public void create(Laptop laptop) {
    log.info("Creating new laptop record for employee ID: {}", laptop.getEmployeeId());
    laptopMapper.insert(laptop);
  }

  @PreAuthorize("HasRole'BOSS")
  public void update(Laptop laptop) {
    log.info("Updating laptop record with ID: {}", laptop.getLaptopId());
    laptopMapper.update(laptop);
  }

  @PreAuthorize("HasRole'BOSS")
  public void delete(Long laptopId) {
    log.info("Deleting laptop record with ID: {}", laptopId);
    laptopMapper.delete(laptopId);
  }
}
