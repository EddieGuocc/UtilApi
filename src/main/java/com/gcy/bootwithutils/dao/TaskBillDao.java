package com.gcy.bootwithutils.dao;

import com.gcy.bootwithutils.model.dto.TaskBillDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskBillDao {

    Integer addTaskRecord(TaskBillDto vo);

    Integer updateTaskRecord(@Param("taskId") int taskId, @Param("finishTime") long finishTime, @Param("dataLength") int dataLength, @Param("status") int status);
}
