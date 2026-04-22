package com.healthapp.itemhealth.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.healthapp.itemhealth.mapper.EmployeeMapper;
import com.healthapp.itemhealth.model.Employee;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

  @Mock private EmployeeMapper employeeMapper;

  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private EmployeeService employeeService;

  @Test
  void getById_Positive_ReturnsEmployee() {
    Employee emp = new Employee();
    emp.setEmployeeId(1L);
    when(employeeMapper.findById(1L)).thenReturn(emp);

    Employee result = employeeService.getById(1L);

    assertNotNull(result);
    assertEquals(1L, result.getEmployeeId());
    verify(employeeMapper, times(1)).findById(1L);
  }

  @Test
  void getById_Negative_NotFoundReturnsNull() {
    when(employeeMapper.findById(99L)).thenReturn(null);
    assertNull(employeeService.getById(99L));
  }

  @Test
  void getEmail_Positive() {
    when(employeeMapper.getEmail(99L)).thenReturn("test@gmail.com");
    String res = employeeService.getEmail(99L);
    assertEquals("test@gmail.com", res);
    verify(employeeMapper).getEmail(99L);
  }

  @Test
  void findAllEmployees_Positive_ReturnsList() {
    List<Employee> list = List.of(new Employee(), new Employee());
    when(employeeMapper.findAllEmployees()).thenReturn(list);

    List<Employee> result = employeeService.findAllEmployees();

    assertEquals(2, result.size());
    verify(employeeMapper, times(1)).findAllEmployees();
  }

  @Test
  void findAllEmployees_Negative_ReturnsEmptyList() {
    when(employeeMapper.findAllEmployees()).thenReturn(List.of());
    assertTrue(employeeService.findAllEmployees().isEmpty());
  }

  @Test
  void findAllEmployeesBoss_Positive_ReturnsList() {
    List<Employee> list = List.of(new Employee());
    when(employeeMapper.findAllEmployeesBoss()).thenReturn(list);

    List<Employee> result = employeeService.findAllEmployeesBoss();

    assertFalse(result.isEmpty());
    verify(employeeMapper, times(1)).findAllEmployeesBoss();
  }

  @Test
  void findBossByEmployeeId_Positive_ReturnsBoss() {
    Employee boss = new Employee();
    boss.setEmployeeId(10L);
    when(employeeMapper.findBossByEmployeeId(5L)).thenReturn(boss);

    Employee result = employeeService.findBossByEmployeeId(5L);

    assertEquals(10L, result.getEmployeeId());
  }

  @Test
  void findBossByEmployeeId_NullPath_ReturnsNull() {
    // Testing behavior when ID is 0 or invalid
    when(employeeMapper.findBossByEmployeeId(0L)).thenReturn(null);
    assertNull(employeeService.findBossByEmployeeId(0L));
  }

  @Test
  void findByUsername_Positive_ReturnsEmployee() {
    Employee emp = new Employee();
    emp.setUsername("test");
    when(employeeMapper.login("test")).thenReturn(emp);

    Employee result = employeeService.findByUsername("test");

    assertEquals("test", result.getUsername());
  }

  @Test
  void findByUsername_NullPath_ReturnsNull() {
    when(employeeMapper.login(null)).thenReturn(null);
    assertNull(employeeService.findByUsername(null));
  }

  @Test
  void insert_Positive_InvokesMapper() {
    Employee emp = new Employee();
    emp.setUsername("new_user");

    employeeService.insert(emp);

    verify(employeeMapper, times(1)).insert(emp);
  }

  @Test
  void delete_Positive_InvokesMapper() {
    employeeService.delete(2L);
    verify(employeeMapper, times(1)).delete(2L);
  }

  @Test
  void update_Positive_InvokesMapper() {
    Employee emp = new Employee();
    emp.setEmployeeId(1L);

    employeeService.update(emp);

    verify(employeeMapper, times(1)).update(emp);
  }

  @Test
  void updatePassword_Positive_EncodesAndSaves() {
    Long id = 1L;
    String raw = "secret123";
    String hashed = "hashed_string";

    when(passwordEncoder.encode(raw)).thenReturn(hashed);

    employeeService.updatePassword(id, raw);

    verify(passwordEncoder, times(1)).encode(raw);
    verify(employeeMapper, times(1)).updatePassword(id, hashed);
  }
}
