package com.gcy.bootwithutils.controller;

import com.gcy.bootwithutils.common.constants.Result;
import com.gcy.bootwithutils.model.dto.StudentDto;
import com.gcy.bootwithutils.model.vo.StudentVo;
import com.gcy.bootwithutils.service.student.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName StudentController
 * @Description TODO
 * @Author Eddie
 * @Date 2021/03/31 15:06
 */

@RestController
@Slf4j
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentService studentService;

    @PostMapping("/list")
    public Result<List<StudentDto>> getStudentList(@RequestBody @Validated StudentVo params) {
        return Result.success(studentService.getStudentList(params));
    }
}
