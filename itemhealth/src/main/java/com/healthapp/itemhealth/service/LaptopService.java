package com.healthapp.itemhealth.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.healthapp.itemhealth.mapper.LaptopMapper;
import com.healthapp.itemhealth.model.Laptop;

@Service
public class LaptopService {

    private final LaptopMapper laptopMapper;

    public LaptopService(LaptopMapper laptopMapper) {
        this.laptopMapper = laptopMapper;
    }

    public Laptop getById(Long laptopId) {
        return laptopMapper.findById(laptopId);
    }

    public List<Laptop> getAll() {
        return laptopMapper.findAll();
    }

    public Laptop getByEmployeeId(Long employeeId) {
        return laptopMapper.findByEmployeeId(employeeId);
    }

    public List<Laptop> getNeedToUpdate() {
        return laptopMapper.findNeedToUpdate();
    }

    public List<Laptop> getInUse() {
        return laptopMapper.findInUse();
    }

    public List<Laptop> getToRenew() {
        return laptopMapper.findToRenew();
    }

    public void create(Laptop laptop) {
        laptopMapper.insert(laptop);
    }

    public void update(Laptop laptop) {
        laptopMapper.update(laptop);
    }

    public void delete(Long laptopId) {
        laptopMapper.delete(laptopId);
    }
}
