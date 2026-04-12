package com.healthapp.itemhealth.controller;

import com.healthapp.itemhealth.model.IDCard;
import com.healthapp.itemhealth.service.IDCardService;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/id-card")
@Validated
public class IDCardController {

  private static final Logger log = LoggerFactory.getLogger(IDCardController.class);
  private final IDCardService idCardService;

  public IDCardController(IDCardService idCardService) {
    this.idCardService = idCardService;
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    StringTrimmerEditor stringTrimmer = new StringTrimmerEditor(true);
    binder.registerCustomEditor(String.class, stringTrimmer);
  }

  @GetMapping("/{idCardId}")
  public ResponseEntity<IDCard> getById(@PathVariable Long idCardId) {
    log.info("Fetching ID card with ID: {}", idCardId);
    return ResponseEntity.ok(idCardService.getById(idCardId));
  }

  @GetMapping
  public ResponseEntity<List<IDCard>> getAll() {
    log.info("Fetching all ID cards");
    return ResponseEntity.ok(idCardService.getAll());
  }

  @GetMapping("/employee/{employeeId}")
  public ResponseEntity<IDCard> getByEmployeeId(@PathVariable Long employeeId) {
    log.info("Fetching ID card for employee ID: {}", employeeId);
    return ResponseEntity.ok(idCardService.getByEmployeeId(employeeId));
  }

  @GetMapping("/in-use")
  public ResponseEntity<List<IDCard>> getInUse() {
    log.info("Fetching all ID cards currently in use");
    return ResponseEntity.ok(idCardService.getInUse());
  }

  @GetMapping("/to-renew")
  public ResponseEntity<List<IDCard>> getToRenew() {
    log.info("Fetching ID cards requiring renewal");
    return ResponseEntity.ok(idCardService.getToRenew());
  }

  @PostMapping
  public ResponseEntity<IDCard> create(@Valid @RequestBody IDCard idCard) {
    log.info("Creating new ID card: {}", idCard);
    idCardService.create(idCard);
    return ResponseEntity.status(201).body(idCard);
  }

  @PatchMapping
  public ResponseEntity<IDCard> update(@Valid @RequestBody IDCard idCard) {
    log.info("Updating ID card: {}", idCard);
    idCardService.update(idCard);
    return ResponseEntity.ok().body(idCard);
  }

  @DeleteMapping("/{idCardId}")
  public ResponseEntity<Void> delete(@PathVariable Long idCardId) {
    log.info("Deleting ID card with ID: {}", idCardId);
    idCardService.delete(idCardId);
    return ResponseEntity.noContent().build();
  }
}
