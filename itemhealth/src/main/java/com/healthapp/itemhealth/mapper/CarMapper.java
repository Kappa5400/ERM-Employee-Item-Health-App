package com.healthapp.itemhealth.mapper;

import com.healthapp.itemhealth.model.Car;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CarMapper {
  Car findById(Long carId);

  List<Car> findAll();

  List<Car> findInUse();

  List<Car> findToService();

  List<Car> findToRenewInsurance();

  void insert(Car car);

  void update(Car car);

  void delete(Long carId);
}
