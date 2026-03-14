package com.healthapp.itemhealth.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.healthapp.itemhealth.mapper.EmployeeMapper;
import com.healthapp.itemhealth.model.Employee;

@Service
public class EmployeeService {

    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    public Employee getById(long employeeId) {
        return employeeMapper.findById(employeeId);
    }

    public List<Employee> findAllEmployees(){
        return employeeMapper.findAllEmployees();
    }

    public List<Employee> findAllEmployeesBoss(){
        return employeeMapper.findAllEmployeesBoss();
    }

    public Employee findBossByEmployeeId(long employeeId){
        return employeeMapper.findBossByEmployeeId(employeeId);
    }

    public void insert(Employee employee){
        employeeMapper.insert(employee);
    }

    public void delete(Long employeeId){
        employeeMapper.delete(employeeId);
    }

    

}
