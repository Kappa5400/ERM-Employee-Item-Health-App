package com.healthapp.itemhealth.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.healthapp.itemhealth.model.Car;

@Mapper
public interface CarMapper {
  Car findById(Long carId);

  Car findByEmployeeId(Long employeeId);

  List<Car> findAll();

  List<Car> findInUse();

  List<Car> findToService();

  List<Car> findToRenewInsurance();

  void insert(Car car);

  void update(Car car);

  void delete(Long carId);
}
