package com.healthapp.itemhealth.mapper;

import com.healthapp.itemhealth.model.Car;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface CarMapper {
  Car findById(Long carId);

  Car findByEmployeeId(Long employeeId);

  List<Car> findAll();

  List<Car> findInUse();

  List<Car> findToService();

  List<Car> findToRenewInsurance();

  void setInsurance(@Param("carId") Long carId, @Param("state") boolean state);

  void setService(@Param("carId") Long carId, @Param("state") boolean state);

  void setReplace(@Param("carId") Long carId, @Param("state") boolean state);


  void insert(Car car);


  void update(Car car);


  void delete(Long carId);
}
