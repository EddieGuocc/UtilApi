package com.gcy.bootwithutils.service.bill.task;

import com.gcy.bootwithutils.model.dto.TaskBillDto;
import com.gcy.bootwithutils.model.dto.ThirdPartBillDto;
import com.gcy.bootwithutils.service.bill.CIBNBillService;
import com.gcy.bootwithutils.service.bill.TaskBillService;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RerunBillTaskThread implements Runnable{

    private static int RUN_TASK_SUCCESS = 1;
    private static int RUN_TASK_FAIL = 2;

    private Object clazz;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] args;

    private TaskBillService taskBillService;

    private int taskId;

    public RerunBillTaskThread(TaskBillService service, Object clazz, String methodName, Class<?>[] parameterTypes,Integer taskId, Object... args) {
        this.taskBillService = service;
        this.clazz = clazz;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.args = args;
        this.taskId = taskId;
    }

    @Override
    public void run() {
        System.out.println("异步任务线程启动======");
        int res = 0;
        try {
            Method method = clazz.getClass().getDeclaredMethod(methodName, parameterTypes);
            res += (int) method.invoke(clazz, args);
            System.out.println("添加异步任务记录=======");
            Thread.sleep(10000);
            taskBillService.updateTaskRecord(taskId, System.currentTimeMillis() / 1000, res, RUN_TASK_SUCCESS);
        } catch (Throwable e) {
            e.printStackTrace();
            taskBillService.updateTaskRecord(taskId, System.currentTimeMillis() / 1000, res, RUN_TASK_FAIL);
        }

        System.out.println("异步任务线程执行完毕======");
    }
}
