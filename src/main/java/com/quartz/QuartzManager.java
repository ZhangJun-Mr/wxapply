package com.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class QuartzManager {
    @Autowired
    private Scheduler scheduler;

    public QuartzManager() throws SchedulerException {
    }

    public void createJob() throws SchedulerException {
        JobKey jobKey = new JobKey("myjob", "myjobgruop");
        JobDetail jobDetail = JobBuilder.newJob(MyJob.class).withIdentity(jobKey).build();

        Map<String, String> map = new HashMap<>();
        jobDetail.getJobDataMap().putAll(map);

        TriggerKey triggerKey = new TriggerKey("mytrigger", "mytriggergroup");
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(CronScheduleBuilder.cronSchedule("*/5 * * * * ?"))
                .build();
        scheduler.scheduleJob(jobDetail, trigger);

    }
}
