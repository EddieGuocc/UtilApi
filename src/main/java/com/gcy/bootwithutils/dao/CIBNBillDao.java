package com.gcy.bootwithutils.dao;

import com.gcy.bootwithutils.model.dto.CIBNBillDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CIBNBillDao {

    Integer insert(CIBNBillDto dto);

    List<CIBNBillDto> select(Integer id);
}
