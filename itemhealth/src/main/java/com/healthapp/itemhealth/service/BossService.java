package com.healthapp.itemhealth.service;

import com.healthapp.itemhealth.mapper.BossMapper;
import com.healthapp.itemhealth.model.Boss;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BossService {

    private static final Logger log = LoggerFactory.getLogger(BossService.class);
    private final BossMapper bossMapper;

    public BossService(BossMapper bossMapper) {
        this.bossMapper = bossMapper;
    }

    public Boss findById(Long bossId) {
        log.debug("Finding boss by ID: {}", bossId);
        return bossMapper.findById(bossId);
    }

    public List<Boss> findAll() {
        log.debug("Finding all bosses");
        return bossMapper.findAll();
    }

    public List<Long> findSubordinateIds(Long bossId) {
        log.debug("Finding subordinate IDs for boss ID: {}", bossId);
        return bossMapper.findSubordinateIds(bossId);
    }

    @PreAuthorize("hasRole('BOSS')")
    @Transactional
    public void insert(Boss boss) {
        log.info("Inserting new boss record for employee: {}", boss.getEmployeeId());
        bossMapper.insert(boss);
        log.info("Successfully inserted boss with generated ID: {}", boss.getBossId());
    }

    @PreAuthorize("hasRole('BOSS')")
    @Transactional
    public void insertSubordinate(Long bossId, long subordinateId) {
        log.info("Linking subordinate {} to boss {}", subordinateId, bossId);
        bossMapper.insertSubordinate(bossId, subordinateId);
    }

    @PreAuthorize("hasRole('BOSS')")
    @Transactional
    public void update(Boss boss) {
        log.info("Updating boss record for ID: {}", boss.getBossId());
        bossMapper.update(boss);
    }

    @PreAuthorize("hasRole('BOSS')")
    @Transactional
    public void delete(Long bossId) {
        log.info("Deleting boss record with ID: {}", bossId);
        bossMapper.delete(bossId);
    }

    @PreAuthorize("hasRole('BOSS')")
    @Transactional
    public void deleteSubordinate(Long bossId, Long subordinateId) {
        log.info("Removing subordinate {} from boss {}", subordinateId, bossId);
        bossMapper.deleteSubordinate(bossId, subordinateId);
    }

    @PreAuthorize("hasRole('BOSS')")
    @Transactional
    public void deleteAllSubordinates(Long bossId) {
        log.info("Removing all subordinates for boss ID: {}", bossId);
        bossMapper.deleteAllSubordinates(bossId);
    }
}