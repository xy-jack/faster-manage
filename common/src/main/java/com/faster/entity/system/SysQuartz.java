package com.faster.entity.system;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * quartz扩展表：保存定时任务相关信息到数据库当中
 *
 * @author  YD
 * @date    2020/9/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysQuartz extends Model<SysQuartz> {

    /** 任务id */
    private String id;

    /** 任务名称 */
    private String jobName;

    /** 任务执行类 */
    private String jobClass;

    /** 任务状态 启动还是暂停*/
    private Integer status;

    /** 任务运行时间表达式 */
    private String cronExpression;

    /** 首次执行时间 */
    private LocalDateTime firstExecuteTime;

    /** 上次执行时间 */
    private LocalDateTime lastExecuteTime;

    /** 下次执行时间 */
    private LocalDateTime nextExecuteTime;

    /** 创建时间 */
    private LocalDateTime createTime;

}
