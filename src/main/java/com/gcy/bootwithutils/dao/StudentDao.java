package com.gcy.bootwithutils.dao;

import com.gcy.bootwithutils.model.dto.StudentDto;
import com.gcy.bootwithutils.model.vo.StudentVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentDao {

    List<StudentDto> getStudentList(StudentVo params);
}
