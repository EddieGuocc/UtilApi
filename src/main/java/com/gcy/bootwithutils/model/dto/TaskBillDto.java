package com.gcy.bootwithutils.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class TaskBillDto {

    private int taskId;

    private long createTime;

    private String className;

    private String methodName;

    private int dataLength;

    public TaskBillDto(long createTime, String className, String methodName) {
        this.createTime = createTime;
        this.className = className;
        this.methodName = methodName;
    }
}
