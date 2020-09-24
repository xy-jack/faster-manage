package com.faster.system.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 测试定时任务是否执行
 *
 * @author da.yang@hand-china.com
 * @date 2020/9/24
 */
public class TestJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("============================定时任务执行成功!=====================");
    }

}
