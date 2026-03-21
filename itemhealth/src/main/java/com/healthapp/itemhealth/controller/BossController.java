package com.healthapp.itemhealth.controller;

import com.healthapp.itemhealth.model.Boss;
import com.healthapp.itemhealth.service.BossService;
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
@RequestMapping("/api/boss")
@Validated
public class BossController {

  private static final Logger log = LoggerFactory.getLogger(BossController.class);

  private final BossService bossService;

  public BossController(BossService bossService) {
    this.bossService = bossService;
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    StringTrimmerEditor stringTrimmer = new StringTrimmerEditor(true);
    binder.registerCustomEditor(String.class, stringTrimmer);
  }

  @GetMapping("/{bossId}")
  public ResponseEntity<Boss> getById(@PathVariable Long bossId) {
    log.debug("Getting boss by ID: {}", bossId);
    return ResponseEntity.ok(bossService.findById(bossId));
  }

  @GetMapping
  public ResponseEntity<List<Boss>> findAll() {
    log.debug("Getting all bosses");
    return ResponseEntity.ok(bossService.findAll());
  }

  @GetMapping("/sub/{bossId}")
  public ResponseEntity<List<Long>> findSubordinateIds(@PathVariable Long bossId) {
    log.debug("Getting subordinate by boss id: {}", bossId);
    return ResponseEntity.ok(bossService.findSubordinateIds(bossId));
  }

  @PostMapping
  public ResponseEntity<Void> insert(@Valid @RequestBody Boss boss) {
    log.info("Inserting boss object: {}", boss);
    bossService.insert(boss);
    return ResponseEntity.status(201).build();
  }

  @PostMapping("/sub/{bossId}/{subordinateId}")
  public ResponseEntity<Void> insertSubordinate(
      @PathVariable Long bossId, @PathVariable Long subordinateId) {
    log.info("Getting boss by subordinate ID: {}", subordinateId);
    bossService.insertSubordinate(bossId, subordinateId);
    return ResponseEntity.status(201).build();
  }

  @PatchMapping()
  public ResponseEntity<Void> update(@Valid @RequestBody Boss boss) {
    log.info("Updating boss: ", boss);
    bossService.update(boss);
    return ResponseEntity.status(200).build();
  }

  @DeleteMapping("/{bossId}")
  public ResponseEntity<Void> delete(@PathVariable Long bossId) {
    log.info("Deleting boss id: {}", bossId);
    bossService.delete(bossId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{bossId}/sub/{subordinateId}")
  public ResponseEntity<Void> deleteSubordinate(
      @PathVariable Long bossId, @PathVariable Long subordinateId) {
    log.info("Deleting subordinate: {}", subordinateId);
    bossService.deleteSubordinate(bossId, subordinateId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{bossId}/sub")
  public ResponseEntity<Void> deleteAllSubordinates(@PathVariable Long bossId) {
    log.info("Deleting all subordinates: {}", bossId);
    bossService.deleteAllSubordinates(bossId);
    return ResponseEntity.noContent().build();
  }
}
