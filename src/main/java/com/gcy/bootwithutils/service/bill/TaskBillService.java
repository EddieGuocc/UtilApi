package com.gcy.bootwithutils.service.bill;

import com.gcy.bootwithutils.dao.TaskBillDao;
import com.gcy.bootwithutils.model.dto.TaskBillDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskBillService {

    @Autowired
    private TaskBillDao taskBillDao;

    @Transactional
    public Integer addTaskRecord(TaskBillDto vo) {
        return taskBillDao.addTaskRecord(vo);
        // throw new RuntimeException("1233");
    }

    @Transactional
    public boolean updateTaskRecord(int taskId, long finishTime, int dataLength, int status) {
        return taskBillDao.updateTaskRecord(taskId, finishTime, dataLength, status) == 1;
    }
}
