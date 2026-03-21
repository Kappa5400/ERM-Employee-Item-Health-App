package com.healthapp.itemhealth.mapper;

import com.healthapp.itemhealth.model.Employee;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EmployeeMapper {
  Employee findById(Long employeeId);

  List<Employee> findAllEmployees();

  List<Employee> findAllEmployeesBoss();

  Employee findBossByEmployeeId(Long employeeId);

  void insert(Employee employee);

  void delete(Long employeeId);

  void update(Employee employee);

  Employee login(@Param("username") String username);

  void updatePassword(Long employeeId, String password);
}
