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

import com.healthapp.itemhealth.model.Boss;
import com.healthapp.itemhealth.service.BossService;



@RestController
@RequestMapping("/api/boss")
public class BossController {

    private final BossService bossService;

    public BossController(BossService bossService) {
        this.bossService = bossService;
    }

    @GetMapping("/{bossId}")
    public ResponseEntity <Boss> getById(@PathVariable Long bossId) {
        return ResponseEntity.ok(bossService.findById(bossId));
    }

    @GetMapping("/all")
    public ResponseEntity <List<Boss>> findAll() {
        return ResponseEntity.ok(bossService.findAll());
    }
    
    @GetMapping("/sub/{bossId}")
    public ResponseEntity <List<Long>> findSubordinateIds(@PathVariable Long bossId){
        return ResponseEntity.ok(bossService.findSubordinateIds(bossId));
    }

    @PostMapping
    public ResponseEntity <Void> insert(@RequestBody Boss boss) {
        bossService.insert(boss);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/sub/{bossid}/{subordinateId}")
    public ResponseEntity <Void> insertSubordinate(@PathVariable Long bossId, @PathVariable Long subordinateId) {
        bossService.insertSubordinate(bossId, subordinateId);
        return ResponseEntity.status(201).build();
    }

    @PatchMapping()
    public ResponseEntity <Void> update(@RequestBody Boss boss){
        bossService.update(boss);
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/{bossId}")
    public ResponseEntity <Void> delete(@PathVariable Long bossId){
        bossService.delete(bossId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{bossId}/sub/{subordinateId}")
    public ResponseEntity <Void> deleteSubordinate(@PathVariable Long bossId, @PathVariable Long subordinateId){
        bossService.deleteSubordinate(bossId, subordinateId);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{bossId}/sub")
    public ResponseEntity <Void> deleteAllSubordinates(@PathVariable Long bossId){
        bossService.deleteAllSubordinates(bossId);
        return ResponseEntity.noContent().build();
    }


}
