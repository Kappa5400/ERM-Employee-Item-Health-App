package com.healthapp.itemhealth.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.healthapp.itemhealth.model.IDCard;


@Mapper
public interface IDCardMapper {
    void insert(IDCard Idcard);
    void update(IDCard Idcard);
    void delete(IDCard Idcard);
} 
