package com.quartz;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("quartz")
public class quartzController {

    @Autowired
    QuartzManager quartzManager;

    @RequestMapping("/createJob")
    public void createJob() throws SchedulerException {
        quartzManager.createJob();
    }
}
