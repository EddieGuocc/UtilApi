package com.gcy.bootwithutils.service.student;

import com.gcy.bootwithutils.dao.StudentDao;
import com.gcy.bootwithutils.model.dto.StudentDto;
import com.gcy.bootwithutils.model.vo.StudentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName StudentService
 * @Description TODO
 * @Author Eddie
 * @Date 2021/03/31 15:11
 */
@Service
public class StudentService {

    @Autowired
    StudentDao studentDao;

    public List<StudentDto> getStudentList(StudentVo params) {
        return studentDao.getStudentList(params);
    }

}
