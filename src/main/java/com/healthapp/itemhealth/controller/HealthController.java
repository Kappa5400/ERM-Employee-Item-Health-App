package com.healthapp.itemhealth.controller;

import com.healthapp.itemhealth.model.Car;
import com.healthapp.itemhealth.model.Employee;
import com.healthapp.itemhealth.model.IDCard;
import com.healthapp.itemhealth.model.Laptop;
import com.healthapp.itemhealth.service.CarService;
import com.healthapp.itemhealth.service.EmployeeService;
import com.healthapp.itemhealth.service.HealthCheckService;
import com.healthapp.itemhealth.service.IDCardService;
import com.healthapp.itemhealth.service.LaptopService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class HealthController {

  private final EmployeeService employeeService;
  private final LaptopService laptopService;
  private final CarService carService;
  private final IDCardService idCardService;
  private final HealthCheckService healthCheckService;

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
          case "id-card" -> idCardService.getById(id);
          default -> null;
        };

    model.addAttribute("item", item);
    return "itemedit";
  }

  @GetMapping("{type}/create/{empid}")
  @PreAuthorize("hasRole('BOSS')")
  public String itemcreate(@PathVariable String type, @PathVariable Long empid, Model model) {
    Employee emp = employeeService.getById(empid);
    model.addAttribute("type", type);
    model.addAttribute("employeeId", empid);
    model.addAttribute("employee", emp);
    model.addAttribute("item", null);
    return "itemcreate";
  }

  @GetMapping("employee/create")
  @PreAuthorize("hasRole('BOSS')")
  public String employeecreate(Model model) {
    model.addAttribute("employee", new Employee());
    model.addAttribute("isCreate", true);
    return "employeecreate";
  }

  @GetMapping("/login")
  public String login() {

    return "login";
  }

  @GetMapping("/runmail")
  @ResponseBody
  public String runmail() {
    healthCheckService.runHealthCheck();
    return "runmail";
  }
}
