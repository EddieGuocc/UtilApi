package com.gcy.bootwithutils.common.configuration.scheduler;

import com.gcy.bootwithutils.service.scheduler.FirstJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName QuaterzConfig
 * @Description TODO
 * @Author Eddie
 * @Date 2021/05/18 17:47
 */
@Configuration
public class QuartzConfig {

    private static String JOB_GROUP_NAME = "JOB-GROUP-NAME";
    private static String TRIGGER_GROUP_NAME = "TRIGGER-GROUP-NAME";

    @Bean
    public JobDetail timeJob() {
        JobDetail jobDetail = JobBuilder
                .newJob(FirstJob.class)
                .withIdentity("timejob1", JOB_GROUP_NAME)
                .usingJobData("username", "eddie")
                .storeDurably().build();
        return jobDetail;
    }

    @Bean
    public Trigger timeTrigger() {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/5 * * * * ?");

        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(timeJob())
                .withIdentity("time trigger", TRIGGER_GROUP_NAME)
                .withSchedule(cronScheduleBuilder)
                .build();
        return trigger;
    }
}
