package com.healthapp.itemhealth.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.healthapp.itemhealth.mapper.BossMapper;
import com.healthapp.itemhealth.mapper.EmployeeMapper;
import com.healthapp.itemhealth.model.Boss;
import com.healthapp.itemhealth.model.Employee;

@Service
public class EmployeeService {

  private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

  private final PasswordEncoder passwordEncoder;
  private final EmployeeMapper employeeMapper;
  private final BossMapper bossMapper;
  private final ExcelService excelService;

  public EmployeeService(
      EmployeeMapper employeeMapper,
      BossMapper bossMapper,
      PasswordEncoder passwordEncoder,
      ExcelService excelService) {
    this.employeeMapper = employeeMapper;
    this.bossMapper = bossMapper;
    this.passwordEncoder = passwordEncoder;
    this.excelService = excelService;
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

  @PreAuthorize("hasRole('BOSS')")
  public void insert(Employee employee) {
    
    // 1. Check for duplicates
    if (employeeMapper.existsByUsername(employee.getUsername())) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, 
            "The username '" + employee.getUsername() + "' is already taken."
        );
    } // <-- Added the closing brace here!

    // 2. Proceed with saving
    log.info("Inserting new employee: {}", employee.getUsername());
    String hashed_password = passwordEncoder.encode(employee.getPassword());
    employee.setPassword(hashed_password);
    employeeMapper.insert(employee);

    // 3. Handle Relationships
    if (employee.isHasBoss()) {
      employeeMapper.insertSub(employee.getBossUserId(), employee.getEmployeeId());
    }

    // 4. Handle Boss Role
    if (employee.isBossRole()) { // Cleaned up the '== true'
      log.info("Turning employee object into boss...");

      Boss boss = new Boss();
      boss.setEmployeeId(employee.getEmployeeId());
      boss.setName(employee.getName());
      boss.setTitle(employee.getTitle());

      bossMapper.insert(boss);
    }
  }

  @PreAuthorize("hasRole('BOSS')")
  public void delete(Long employeeId) {
    log.info("Checking if big boss...");
    Employee emp = getById(employeeId);

    if (emp.isBossRole() && !emp.isHasBoss()) {
      log.info("Is big boss.");
      return;
    }
    log.info("Checking if boss role...");
    
    if (emp.isBossRole()) {
      log.info("Is boss.");
      log.info("Reassigning subordinates to 1...");

      bossMapper.reassignSubordinatesToBigBoss(employeeId);

      log.info("Deleting from boss...");

      bossMapper.delete(bossMapper.findByempId(employeeId).getBossId());

    } else {
      log.info("Not boss.");
    }
    log.info("Deleting employee with ID: {}", employeeId);
    employeeMapper.delete(employeeId);
  }

  @PreAuthorize("hasRole('BOSS')")
  public void update(Employee employee) {
    log.info("Updating employee record for: {}", employee.getUsername());
    employeeMapper.update(employee);
  }

  public Employee findByUsername(String username) {
    log.info("Finding employee by username: {}", username);
    return employeeMapper.login(username);
  }

  @PreAuthorize("hasRole('BOSS')")
  public void updatePassword(Long employeeId, String raw_password) {
    log.info("Updating password for employee ID: {}", employeeId);
    String hashed_password = passwordEncoder.encode(raw_password);
    employeeMapper.updatePassword(employeeId, hashed_password);
  }

  public String getEmail(Long employeeID) {
    log.info("Attempting to retrieve employee " + employeeID + " email...");
    return employeeMapper.getEmail(employeeID);
  }

 

  }



