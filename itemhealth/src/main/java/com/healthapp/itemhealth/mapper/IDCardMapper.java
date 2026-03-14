package com.healthapp.itemhealth.mapper;

import com.healthapp.itemhealth.model.IDCard;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IDCardMapper {
  IDCard findById(Long idCardId);

  List<IDCard> findAll();

  IDCard findByEmployeeId(Long employeeId);

  List<IDCard> findInUse();

  List<IDCard> findToRenew();

  void insert(IDCard idCard);

  void update(IDCard idCard);

  void delete(Long idCardId);
}
