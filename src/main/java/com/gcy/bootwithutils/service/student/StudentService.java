package com.gcy.bootwithutils.service.student;

import cn.hutool.core.util.RandomUtil;
import com.gcy.bootwithutils.dao.StudentDao;
import com.gcy.bootwithutils.model.dto.StudentDto;
import com.gcy.bootwithutils.model.vo.StudentVo;
import com.gcy.bootwithutils.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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

    @Autowired
    FileService fileService;

    public List<StudentDto> getStudentList(StudentVo params) {
        return studentDao.getStudentList(params);
    }


    /*
     *  export file using Mybatis Stream Start ===	1617273178717
     *  export file using Mybatis Stream End ===	1617273185861
     *  Total Time:	7144
     *  需要设定为一个事务，因为返回的是一个迭代器，所以会一直保持与数据库的连接，数据是从迭代器中读取
     */
    @Transactional
    public void exportAllStudentInfoWithStream(HttpServletResponse response) {
        long startTime = System.currentTimeMillis();
        System.out.println("export file using Mybatis Stream Start ===\t" + startTime);
        final StringBuffer stringBuffer = new StringBuffer();
        studentDao.exportAllStudentWithStream(resultContext -> {
            stringBuffer.append(resultContext.getResultObject().toString());
            stringBuffer.append("\n");
        });
        fileService.exportFile("studentInfo" + System.currentTimeMillis() / 1000, stringBuffer.toString(), response);
        long endTime = System.currentTimeMillis();
        System.out.println("export file using Mybatis Stream End ===\t" + endTime + "\nTotal Time:\t" + (endTime - startTime));
    }

    /*
     *  Caused by: java.lang.OutOfMemoryError: GC overhead limit exceeded
     *  对照组，堆空间分配128M时，内存溢出
     */
    public void exportAllStudentInfo(HttpServletResponse response) {
        long startTime = System.currentTimeMillis();
        System.out.println("export file using Mybatis Stream Start ===\t" + startTime);
        List<StudentDto> list = studentDao.exportAllStudent();
        StringBuilder stringBuffer = new StringBuilder();
        for (StudentDto studentDto : list) {
            stringBuffer.append(studentDto.toString());
            stringBuffer.append("\n");
        }
        fileService.exportFile("studentInfo" + System.currentTimeMillis() / 1000, stringBuffer.toString(), response);
        long endTime = System.currentTimeMillis();
        System.out.println("export file End ===\t" + endTime + "\nTotal Time:\t" + (endTime - startTime));
    }

    @Transactional
    public Boolean insertStudent(Integer count) {
        List<StudentVo> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
         list.add(new StudentVo(RandomUtil.randomString(7), RandomUtil.randomInt(30)));
        }
        return list.size() == studentDao.insertStudent(list);
    }



}
