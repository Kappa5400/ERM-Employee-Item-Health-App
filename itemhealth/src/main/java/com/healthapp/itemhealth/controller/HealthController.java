package com.healthapp.itemhealth.controller;

import com.healthapp.itemhealth.model.Car;
import com.healthapp.itemhealth.model.Employee;
import com.healthapp.itemhealth.model.IDCard;
import com.healthapp.itemhealth.model.Laptop;
import com.healthapp.itemhealth.service.CarService;
import com.healthapp.itemhealth.service.EmployeeService;
import com.healthapp.itemhealth.service.IDCardService;
import com.healthapp.itemhealth.service.LaptopService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class HealthController {

  private final EmployeeService employeeService;
  private final LaptopService laptopService;
  private final CarService carService;
  private final IDCardService idCardService;

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

  @GetMapping("/view/employee/{id}")
  public String employeePage(@PathVariable Long id, Model model) {

    Employee emp = employeeService.getById(id);
    Laptop lap = laptopService.getByEmployeeId(id);
    Car car = carService.getByEmployeeId(id);
    IDCard idcard = idCardService.getByEmployeeId(id);

    model.addAttribute("employee", emp);
    model.addAttribute("laptop", lap);
    model.addAttribute("car", car);
    model.addAttribute("idcard", idcard);

    return "employeepage";
  }

  @GetMapping("/view/boss/{id}")
  @PreAuthorize("hasRole('BOSS')")
  public String bossEmployeePage(@PathVariable Long id, Model model) {

    Employee emp = employeeService.getById(id);
    Laptop lap = laptopService.getByEmployeeId(id);
    Car car = carService.getByEmployeeId(id);
    IDCard idcard = idCardService.getByEmployeeId(id);

    model.addAttribute("employee", emp);
    model.addAttribute("laptop", lap);
    model.addAttribute("car", car);
    model.addAttribute("idcard", idcard);

    return "bossemployeepage";
  }

  @GetMapping("/{type}/edit/{id}")
  @PreAuthorize("hasRole('BOSS')")
  public String itemedit(@PathVariable String type, @PathVariable Long id, Model model) {

    Object item =
        switch (type) {
          case "laptop" -> laptopService.getById(id);
          case "car" -> carService.getById(id);
          case "idcard" -> idCardService.getById(id);
          default -> null;
        };

    model.addAttribute("item", item);
    return "itemedit";
  }

  // to add: add employee, add boss, add item pages
  // delete, update, edit pages

  // make update create page same make template or fragment
  // one page shows each employee each item, button
  // button to add, edit, update employee
  // button to crud items
  // add employee page dynamic, shows all items in possesion and all employee data

}
