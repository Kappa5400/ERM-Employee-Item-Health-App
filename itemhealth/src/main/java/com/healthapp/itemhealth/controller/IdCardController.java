package com.healthapp.itemhealth.controller;

import com.healthapp.itemhealth.model.IDCard;
import com.healthapp.itemhealth.service.IDCardService;
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

@RestController
@RequestMapping("/api/id-cards")
public class IdCardController {

  private final IDCardService idCardService;

  public IdCardController(IDCardService idCardService) {
    this.idCardService = idCardService;
  }

  @GetMapping("/{idCardId}")
  public ResponseEntity<IDCard> getById(@PathVariable Long idCardId) {
    return ResponseEntity.ok(idCardService.getById(idCardId));
  }

  @GetMapping
  public ResponseEntity<List<IDCard>> getAll() {
    return ResponseEntity.ok(idCardService.getAll());
  }

  @GetMapping("/employee/{employeeId}")
  public ResponseEntity<IDCard> getByEmployeeId(@PathVariable Long employeeId) {
    return ResponseEntity.ok(idCardService.getByEmployeeId(employeeId));
  }

  @GetMapping("/in-use")
  public ResponseEntity<List<IDCard>> getInUse() {
    return ResponseEntity.ok(idCardService.getInUse());
  }

  @GetMapping("/to-renew")
  public ResponseEntity<List<IDCard>> getToRenew() {
    return ResponseEntity.ok(idCardService.getToRenew());
  }

  @PostMapping
  public ResponseEntity<Void> create(@RequestBody IDCard idCard) {
    idCardService.create(idCard);
    return ResponseEntity.status(201).build();
  }

  @PatchMapping
  public ResponseEntity<Void> update(@RequestBody IDCard idCard) {
    idCardService.update(idCard);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{idCardId}")
  public ResponseEntity<Void> delete(@PathVariable Long idCardId) {
    idCardService.delete(idCardId);
    return ResponseEntity.noContent().build();
  }
}
