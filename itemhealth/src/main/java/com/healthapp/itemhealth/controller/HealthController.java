package com.healthapp.itemhealth.controller;

import com.healthapp.itemhealth.model.Employee;
import com.healthapp.itemhealth.service.EmployeeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HealthController {

  private final EmployeeService employeeService;

  @GetMapping("/")
  public String showDashboard(Model model) {

    List<Employee> employees = employeeService.findAllEmployees();

    model.addAttribute("employeeList", employees);

    return "dashboard";
  }

  @GetMapping("/bossdashboard")
  @PreAuthorize("hasRole('BOSS')")
  public String showBossDashboard(Model model) {

    List<Employee> employees = employeeService.findAllEmployees();

    model.addAttribute("employeeList", employees);

    return "bossdashboard";
  }
}
