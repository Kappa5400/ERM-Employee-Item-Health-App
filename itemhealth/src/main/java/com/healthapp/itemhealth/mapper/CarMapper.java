package com.healthapp.itemhealth.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.healthapp.itemhealth.model.Car;

@Mapper
public interface CarMapper {
    void insert(Car car);
    void delete(Long carId);
}
