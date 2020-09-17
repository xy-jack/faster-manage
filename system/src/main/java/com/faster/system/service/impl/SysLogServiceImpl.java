package com.faster.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.faster.entity.system.SysLog;
import com.faster.system.mapper.SysLogMapper;
import com.faster.system.service.ISysLogService;
import com.faster.utils.Result;
import org.springframework.stereotype.Service;

/**
 * 日志表 服务实现类
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    @Override
    public Result<Boolean> saveLog(SysLog sysLog, String from) {
        return null;
    }
}
