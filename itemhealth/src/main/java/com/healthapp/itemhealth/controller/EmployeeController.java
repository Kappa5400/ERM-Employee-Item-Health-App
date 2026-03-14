package com.healthapp.itemhealth.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthapp.itemhealth.model.Employee;
import com.healthapp.itemhealth.service.EmployeeService;



@RestController
@RequestMapping("/api/employee")
public class EmployeeController {


    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService, EmployeeService employeeService_1) {
        this.employeeService = employeeService;

    }

    @GetMapping("{id}")
    public ResponseEntity <Employee> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getById(id));
    }
    
    @GetMapping
    public ResponseEntity <List<Employee>> findAllEmployees() {
        return ResponseEntity.ok(employeeService.findAllEmployees());
    }
    
    @GetMapping("/boss")
    public ResponseEntity <List<Employee>> findAllEmployeeasBoss(){
        return ResponseEntity.ok(employeeService.findAllEmployeesBoss());
    }

    @GetMapping ("boss/{employeeId}")
    public ResponseEntity <Employee> findBossByEmployeeId(@PathVariable Long employeeId){
        return ResponseEntity.ok(employeeService.findBossByEmployeeId(employeeId));
    }

    @PostMapping
    public ResponseEntity <Void> insert(@RequestBody Employee employee) {
        employeeService.insert(employee);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <Void> delete(@PathVariable Long id){
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }
    

}
