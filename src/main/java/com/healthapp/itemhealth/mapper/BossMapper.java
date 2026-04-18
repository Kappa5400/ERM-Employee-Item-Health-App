package com.healthapp.itemhealth.mapper;

import com.healthapp.itemhealth.model.Boss;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BossMapper {
  Boss findById(Long bossId);
  
  Boss findByempId(Long bossId);

  List<Boss> findAll();

  List<Long> findSubordinateIds(Long bossId);

  void insert(Boss boss);

  void insertSubordinate(@Param("bossId") Long bossId, @Param("subordinateId") Long subordinateId);

  void update(Boss boss);

  void delete(Long bossId);

  void deleteSubordinate(@Param("bossId") Long bossId, @Param("subordinateId") Long subordinateId);

  void deleteAllSubordinates(Long bossId);
}
