package com.gcy.bootwithutils.dao;

import com.gcy.bootwithutils.model.dto.StudentDto;
import com.gcy.bootwithutils.model.vo.StudentVo;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentDao {

    List<StudentDto> getStudentList(StudentVo params);

    // 流式查询 防止大量数据导致内存溢出
    void exportAllStudentWithStream(ResultHandler<StudentDto> handler);

    Integer insertStudent(List<StudentVo> param);

    List<StudentDto> exportAllStudent();

}
