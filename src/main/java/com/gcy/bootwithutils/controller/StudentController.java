package com.gcy.bootwithutils.controller;

import com.gcy.bootwithutils.common.constants.Result;
import com.gcy.bootwithutils.dao.StudentDao;
import com.gcy.bootwithutils.model.dto.StudentDto;
import com.gcy.bootwithutils.model.vo.StudentVo;
import com.gcy.bootwithutils.service.student.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    StudentDao studentDao;

    @PostMapping("/list")
    public Result<List<StudentDto>> getStudentList(@RequestBody @Validated StudentVo params) {
        return Result.success(studentService.getStudentList(params));
    }

    @GetMapping("/getAllInfoWithStream/file")
    public void exportAllStudentInfoWithStream(HttpServletResponse response) {
        studentService.exportAllStudentInfoWithStream(response);
    }

    @GetMapping("/getAllInfo/file")
    public void exportAllStudentInfo(HttpServletResponse response) {
        studentService.exportAllStudentInfo(response);
    }

    // 批量插入数据
    @PostMapping("/insert")
    public Result<Boolean> insertStudentInfo(@RequestParam("num") Integer num) {
        if (studentService.insertStudent(num)) {
            return Result.success();
        }
        return Result.failed();
    }
}
