package com.healthapp.itemhealth.service;

import com.healthapp.itemhealth.mapper.EmployeeMapper;
import com.healthapp.itemhealth.model.Employee;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

  private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

  private final PasswordEncoder passwordEncoder;
  private final EmployeeMapper employeeMapper;

  public EmployeeService(EmployeeMapper employeeMapper, PasswordEncoder passwordEncoder) {
    this.employeeMapper = employeeMapper;
    this.passwordEncoder = passwordEncoder;
  }

  public Employee getById(long employeeId) {
    log.info("Fetching employee with ID: {}", employeeId);
    return employeeMapper.findById(employeeId);
  }

  public List<Employee> findAllEmployees() {
    log.info("Fetching all employees");
    return employeeMapper.findAllEmployees();
  }

  public List<Employee> findAllEmployeesBoss() {
    log.info("Fetching all employees with boss status");
    return employeeMapper.findAllEmployeesBoss();
  }

  public Employee findBossByEmployeeId(long employeeId) {
    log.info("Fetching boss for employee ID: {}", employeeId);
    return employeeMapper.findBossByEmployeeId(employeeId);
  }

  @PreAuthorize("HasRole'BOSS")
  public void insert(Employee employee) {
    log.info("Inserting new employee: {}", employee.getUsername());
    employeeMapper.insert(employee);
  }

  @PreAuthorize("HasRole'BOSS")
  public void delete(Long employeeId) {
    log.info("Deleting employee with ID: {}", employeeId);
    employeeMapper.delete(employeeId);
  }

  @PreAuthorize("HasRole'BOSS")
  public void update(Employee employee) {
    log.info("Updating employee record for: {}", employee.getUsername());
    employeeMapper.update(employee);
  }

  public Employee findByUsername(String username) {
    log.info("Finding employee by username: {}", username);
    return employeeMapper.login(username);
  }

  @PreAuthorize("HasRole'BOSS")
  public void updatePassword(Long employeeId, String raw_password) {
    log.info("Updating password for employee ID: {}", employeeId);
    String hashed_password = passwordEncoder.encode(raw_password);
    employeeMapper.updatePassword(employeeId, hashed_password);
  }
}
