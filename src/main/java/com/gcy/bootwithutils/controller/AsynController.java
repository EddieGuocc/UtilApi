package com.gcy.bootwithutils.controller;

import com.gcy.bootwithutils.common.constants.Result;
import com.gcy.bootwithutils.model.dto.TaskBillDto;
import com.gcy.bootwithutils.model.vo.AsynTaskVO;
import com.gcy.bootwithutils.service.bill.AllBillService;
import com.gcy.bootwithutils.service.bill.CIBNBillService;
import com.gcy.bootwithutils.service.bill.TaskBillService;
import com.gcy.bootwithutils.service.bill.ThirdPartBillService;
import com.gcy.bootwithutils.service.bill.task.RerunBillTaskThread;
import com.gcy.bootwithutils.utils.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/async")
public class AsynController {

    @Autowired
    private TaskBillService service;

    @Autowired
    private ThirdPartBillService thirdPartBillService;

    @Autowired
    private CIBNBillService cibnBillService;

    @Autowired
    private AllBillService allBillService;

    @GetMapping("/get")
    public Result<Boolean> getAsyncTask(@RequestBody @Validated AsynTaskVO vo) {
        String methodName = "processBill";
        TaskBillDto dto = new TaskBillDto(System.currentTimeMillis() / 1000, thirdPartBillService.toString(), methodName);
        int taskId = service.addTaskRecord(dto);
        if (taskId > 0 && dto.getTaskId() != 0) {
            Class<?>[] parametersTypes = new Class[]{AsynTaskVO.class};
            RerunBillTaskThread taskThread = new RerunBillTaskThread(service, allBillService, methodName, parametersTypes, dto.getTaskId(), vo);
            ThreadUtil.submit(taskThread);
            return Result.success();
        }
        return Result.failed("新增异步任务失败");
    }

    @PutMapping("/set")
    public Result<Boolean> settingAsyncTask() {
        if (cibnBillService.insert()) {
            return Result.success();
        }
        return Result.failed();
    }

}
