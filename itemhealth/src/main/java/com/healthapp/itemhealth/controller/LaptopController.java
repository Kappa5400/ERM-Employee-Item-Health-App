package com.healthapp.itemhealth.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthapp.itemhealth.model.Laptop;
import com.healthapp.itemhealth.service.LaptopService;

@RestController
@RequestMapping("/api/laptops")
public class LaptopController {

    private final LaptopService laptopService;

    public LaptopController(LaptopService laptopService) {
        this.laptopService = laptopService;
    }

    @GetMapping("/{laptopId}")
    public ResponseEntity<Laptop> getById(@PathVariable Long laptopId) {
        return ResponseEntity.ok(laptopService.getById(laptopId));
    }

    @GetMapping
    public ResponseEntity<List<Laptop>> getAll() {
        return ResponseEntity.ok(laptopService.getAll());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Laptop> getByEmployeeId(@PathVariable Long employeeId) {
        return ResponseEntity.ok(laptopService.getByEmployeeId(employeeId));
    }

    @GetMapping("/need-to-update")
    public ResponseEntity<List<Laptop>> getNeedToUpdate() {
        return ResponseEntity.ok(laptopService.getNeedToUpdate());
    }

    @GetMapping("/in-use")
    public ResponseEntity<List<Laptop>> getInUse() {
        return ResponseEntity.ok(laptopService.getInUse());
    }

    @GetMapping("/to-renew")
    public ResponseEntity<List<Laptop>> getToRenew() {
        return ResponseEntity.ok(laptopService.getToRenew());
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Laptop laptop) {
        laptopService.create(laptop);
        return ResponseEntity.status(201).build();
    }

    @PatchMapping
    public ResponseEntity<Void> update(@RequestBody Laptop laptop) {
        laptopService.update(laptop);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{laptopId}")
    public ResponseEntity<Void> delete(@PathVariable Long laptopId) {
        laptopService.delete(laptopId);
        return ResponseEntity.noContent().build();
    }
}
