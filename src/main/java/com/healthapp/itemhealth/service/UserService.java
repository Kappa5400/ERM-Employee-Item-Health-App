package com.healthapp.itemhealth.service;

import com.healthapp.itemhealth.mapper.EmployeeMapper;
import com.healthapp.itemhealth.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// User service handles the login 
// and security logic

@Service
public class UserService implements UserDetailsService {

  private final EmployeeMapper employeeMapper;
  private static final Logger log = LoggerFactory.getLogger(UserService.class);

  public UserService(EmployeeMapper employeeMapper) {
    this.employeeMapper = employeeMapper;
  }


  // Look up the username entered in the front end and if it exists.
  // if it doesn't, throw error.
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Employee employee = employeeMapper.login(username);

    log.debug("DEBUG: Login attempt for username: " + username);

    // If user doesn't exist throw error
    if (employee == null) {
      log.debug("DEBUG: User NOT FOUND in DB for username: " + username);
      throw new UsernameNotFoundException("User not found: " + username);
    }

    // Make spring user object if username exists
    // gets the user's password and role
    return User.builder()
        .username(employee.getUsername())
        .password(employee.getPassword())
        .roles(employee.isBossRole() ? "BOSS" : "EMPLOYEE")
        .build();
  }
}
