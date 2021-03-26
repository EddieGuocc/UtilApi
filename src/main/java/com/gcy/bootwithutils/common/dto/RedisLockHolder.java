package com.gcy.bootwithutils.common.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName RedisLockHolder
 * @Description 资源锁详细信息
 * @Author Eddie
 * @Date 2021/03/21 15:10
 */
@Getter
@Setter
public class RedisLockHolder {

    private String businessKey;

    // 资源锁锁定时长
    private Long lockTime;

    // 上次修改时间
    private Long lastModifyTime;

    private Thread currentThread;

    // 尝试次数上限
    private int tryCount;

    // 当前尝试次数
    private int currentCount;

    // 续时时间长度，单位秒，当前时间进入锁定时间的最后三分之一（也许是业务时间过长），进行续时
    private Long modifyPeriod;

    public RedisLockHolder(String businessKey, Long lockTime, Long lastModifyTime, Thread currentThread, int tryCount) {
        this.businessKey = businessKey;
        this.lockTime = lockTime;
        this.lastModifyTime = lastModifyTime;
        this.currentThread = currentThread;
        this.tryCount = tryCount;
        this.modifyPeriod = lockTime / 3 * 2;
    }
}
