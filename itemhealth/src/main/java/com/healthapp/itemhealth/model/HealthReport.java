package com.healthapp.itemhealth.model;

import com.healthapp.itemhealth.model.Employee;
import com.healthapp.itemhealth.model.Laptop;
import com.healthapp.itemhealth.model.Car;
import com.healthapp.itemhealth.model.IDCard;

import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthReport {
    private Employee employee;
    private Car car;
    private IDCard idCard;
    private Laptop laptop;
    private String itemType;

}
