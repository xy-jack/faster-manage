package com.faster.system.controller;

import com.faster.entity.system.SysQuartz;
import com.faster.system.utils.QuartzUtils;
import com.faster.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * quartz定时任务管理
 *
 * @author YD
 * @date   2020/9/23
 */
@RestController
@RequestMapping("/quartz")
@Slf4j
public class QuartzController {

    @Resource
    @Qualifier("scheduler")
    private Scheduler scheduler;


    @RequestMapping("/createJob")
    public Result createJob(SysQuartz sysQuartz)  {
        try {
            // com.faster.system.quartz.TestJob
            sysQuartz.setJobClass(sysQuartz.getJobClass());
            sysQuartz.setJobName(sysQuartz.getJobName());
            // 每5秒执行一次 0/5 * * * * ?
            sysQuartz.setCronExpression(sysQuartz.getCronExpression());
            QuartzUtils.createScheduleJob(scheduler, sysQuartz);
        } catch (Exception e) {
            log.error("-==============定时任务创建失败：{}", e);
            return Result.failed();
        }
        return Result.ok();
    }

    @RequestMapping("/pauseJob")
    public String  pauseJob()  {
        try {
            QuartzUtils.pauseScheduleJob (scheduler,"TestJob");
        } catch (Exception e) {
            return "暂停失败";
        }
        return "暂停成功";
    }

    @RequestMapping("/runOnce")
    public String  runOnce()  {
        try {
            QuartzUtils.runOnce (scheduler,"TestJob");
        } catch (Exception e) {
            return "运行一次失败";
        }
        return "运行一次成功";
    }

    @RequestMapping("/resume")
    public String  resume()  {
        try {

            QuartzUtils.resumeScheduleJob(scheduler,"TestJob");
        } catch (Exception e) {
            return "启动失败";
        }
        return "启动成功";
    }

    @RequestMapping("/update")
    public String  update(SysQuartz sysQuartz)  {
        try {
            //进行测试所以写死
            sysQuartz.setJobClass("com.faster.system.quartz.TestJob");
            sysQuartz.setJobName("TestJob");
            sysQuartz.setCronExpression("10 * * * * ?");
            QuartzUtils.updateScheduleJob(scheduler, sysQuartz);
        } catch (Exception e) {
            return "启动失败";
        }
        return "启动成功";
    }
}
