package com.healthapp.itemhealth.service;

import com.healthapp.itemhealth.model.Boss;
import com.healthapp.itemhealth.model.Employee;
import com.healthapp.itemhealth.model.HealthReport;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  private final EmployeeService employeeService;

  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String fromEmail;

  public EmailService(JavaMailSender mailSender, EmployeeService employeeService) {
    this.mailSender = mailSender;
    this.employeeService = employeeService;
  }

  public void sendEmail(String to, String subject, String body) {
    SimpleMailMessage mes = new SimpleMailMessage();
    mes.setFrom(fromEmail);
    mes.setTo(to);
    mes.setSubject(subject);
    mes.setText(body);
    mailSender.send(mes);
  }

  public String getBossEmail(Employee employee) {

    Boss boss = employee.getBoss();
    Long bossEmpId = boss.getEmployeeId();
    Employee bossEmp = employeeService.getById(bossEmpId);
    String bossEmail = bossEmp.getEmail();
    return bossEmail;
  }

  public String formatEmailSubject(Employee employee) {
    String name = employee.getName();
    return "Alert, expiring items for " + name + " " + LocalDateTime.now();
  }

  public String formatEmailBody(List<HealthReport> items) {
    if (items == null || items.isEmpty()) return "No items updated.";

    // Get the employee info from the first report
    Employee emp = items.get(0).getEmployee();

    StringBuilder sb = new StringBuilder();
    sb.append("Alert for: ").append(emp.getName()).append("\n");
    sb.append("------------------------------------------\n");

    for (HealthReport report : items) {
      String id = "N/A";

      // Logic to retrieve the ID based on the item type
      if (report.getLaptop() != null) {
        id = String.valueOf(report.getLaptop().getLaptopId());
      } else if (report.getCar() != null) {
        id = String.valueOf(report.getCar().getCarId());
      } else if (report.getIdCard() != null) {
        id = String.valueOf(report.getIdCard().getIdCardId());
      }

      sb.append("- ")
          .append(report.getItemType())
          .append(" (ID: ")
          .append(id)
          .append(") needs to be updated.\n");
    }

    return sb.toString();
  }
}
