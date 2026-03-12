package com.healthapp.itemhealth.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.healthapp.itemhealth.model.Laptop;


@Mapper
public interface LaptopMapper {
    void insert(Laptop laptop);
    void update(Laptop laptop);
    void delete(Long laptopId);
}