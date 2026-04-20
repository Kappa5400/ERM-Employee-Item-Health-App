package com.healthapp.itemhealth.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.healthapp.itemhealth.model.Car;
import com.healthapp.itemhealth.model.Employee;
import com.healthapp.itemhealth.model.IDCard;
import com.healthapp.itemhealth.model.Laptop;

@Service
public class ExcelService {

    public ByteArrayInputStream employeesToExcel(
            List<Employee> employees, 
            List<Laptop> laptops, 
            List<Car> cars, 
            List<IDCard> idCards) {

        try (Workbook workbook = new XSSFWorkbook(); 
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // Create a shared date style for readability
            CellStyle dateStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd"));

            // --- 1. EMPLOYEES ---
            Sheet empSheet = workbook.createSheet("Employees");
            writeHeader(empSheet, new String[]{"ID", "Name", "Title", "Email", "isBoss"});
            int rowIdx = 1;
            for (Employee e : employees) {
                Row row = empSheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(e.getEmployeeId() != null ? e.getEmployeeId() : 0);
                row.createCell(1).setCellValue(e.getName());
                row.createCell(2).setCellValue(e.getTitle());
                row.createCell(3).setCellValue(e.getEmail());
                row.createCell(4).setCellValue(e.isBossRole() ? "Yes" : "No");
            }

            // --- 2. LAPTOPS ---
            Sheet lapSheet = workbook.createSheet("Laptops");
            writeHeader(lapSheet, new String[]{"ID", "Emp ID", "Year", "OS Ver", "Last Update", "Need Update", "To Renew", "In Use"});
            rowIdx = 1;
            for (Laptop l : laptops) {
                Row row = lapSheet.createRow(rowIdx++);
                // Note: Using getLaptopId() - Check your model if it's getLaptopId or getLaptopId
                row.createCell(0).setCellValue(l.getLaptopId() != null ? l.getLaptopId() : 0);
                row.createCell(1).setCellValue(l.getEmployeeId());
                row.createCell(2).setCellValue(l.getLaptopYear());
                row.createCell(3).setCellValue(l.getOsVersion());
                addDateCell(row, 4, l.getLastOSUpdate(), dateStyle);
                row.createCell(5).setCellValue(l.isNeedToUpdate() ? "YES" : "No");
                row.createCell(6).setCellValue(l.isToRenew() ? "YES" : "No");
                row.createCell(7).setCellValue(l.isInUse() ? "Yes" : "No");
            }

            // --- 3. CARS ---
            Sheet carSheet = workbook.createSheet("Cars");
            writeHeader(carSheet, new String[]{"ID", "Emp ID", "Year", "Mileage", "To Replace", "Last Service", "Next Service", "Insurance Expire", "In Use"});
            rowIdx = 1;
            for (Car c : cars) {
                Row row = carSheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(c.getCarId() != null ? c.getCarId() : 0);
                row.createCell(1).setCellValue(c.getEmployeeId() != null ? c.getEmployeeId() : 0);
                row.createCell(2).setCellValue(c.getCarYear());
                row.createCell(3).setCellValue(c.getMilage());
                row.createCell(4).setCellValue(c.isToReplace() ? "REPLACE" : "-");
                addDateCell(row, 5, c.getLastServiced(), dateStyle);
                addDateCell(row, 6, c.getNeedToServiceDate(), dateStyle);
                addDateCell(row, 7, c.getInsuranceExpireDate(), dateStyle);
                row.createCell(8).setCellValue(c.isInUse() ? "Active" : "No");
            }

            // --- 4. ID CARDS ---
            Sheet idSheet = workbook.createSheet("ID Cards");
            writeHeader(idSheet, new String[]{"ID", "Emp ID", "Last Renewed", "Next Renewal", "In Use", "To Renew"});
            rowIdx = 1;
            for (IDCard id : idCards) {
                Row row = idSheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(id.getIdCardId() != null ? id.getIdCardId() : 0);
                row.createCell(1).setCellValue(id.getEmployeeId() != null ? id.getEmployeeId() : 0);
                addDateCell(row, 2, id.getLastRenewedDate(), dateStyle);
                addDateCell(row, 3, id.getNeedToRenewDate(), dateStyle);
                row.createCell(4).setCellValue(id.isInUse() ? "Active" : "Inactive");
                row.createCell(5).setCellValue(id.isToRenew() ? "YES" : "No");
            }

            // Auto-size columns for a clean look
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet s = workbook.getSheetAt(i);
                if (s.getRow(0) != null) {
                    for (int j = 0; j < s.getRow(0).getPhysicalNumberOfCells(); j++) {
                        s.autoSizeColumn(j);
                    }
                }
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Fail to export data to Excel", e);
        }
    }

    private void writeHeader(Sheet sheet, String[] headers) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
    }

    private void addDateCell(Row row, int column, Object date, CellStyle style) {
        if (date == null) return;
        Cell cell = row.createCell(column);
        if (date instanceof java.time.LocalDate) {
            cell.setCellValue((java.time.LocalDate) date);
        } else if (date instanceof java.time.LocalDateTime) {
            cell.setCellValue((java.time.LocalDateTime) date);
        }
        cell.setCellStyle(style);
    }



}