package com.healthapp.itemhealth.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.healthapp.itemhealth.model.Laptop;

@Mapper
public interface LaptopMapper {
  Laptop findById(Long LaptopId);

  List<Laptop> findAll();

  Laptop findByEmployeeId(Long employeeId);

  List<Laptop> findNeedToUpdate();

  List<Laptop> findInUse();

  List<Laptop> findToRenew();

  void insert(Laptop laptop);

  void setRenew(@Param("laptopId") long LaptopId, @Param("state") boolean state);

  void setOSUpdate(@Param("laptopId") long LaptopId, @Param("state") boolean state);

  void update(Laptop laptop);

  void delete(Long laptopId);
}
