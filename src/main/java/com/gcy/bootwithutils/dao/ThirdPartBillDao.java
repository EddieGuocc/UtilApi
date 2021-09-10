package com.gcy.bootwithutils.dao;

import com.gcy.bootwithutils.model.dto.ThirdPartBillDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThirdPartBillDao {

    List<ThirdPartBillDto> select(Integer id);

    Integer insert(ThirdPartBillDto dto);
}
