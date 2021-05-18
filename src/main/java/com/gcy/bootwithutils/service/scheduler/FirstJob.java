package com.gcy.bootwithutils.service.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName FirstJob
 * @Description TODO
 * @Author Eddie
 * @Date 2021/05/18 16:56
 */
public class FirstJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String userName = (String) jobExecutionContext.getJobDetail().getJobDataMap().get("username");
        System.out.println("now time from scheduler:" + time + "\tedit by" + userName);
    }
}
