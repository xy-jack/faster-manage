package com.faster.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.faster.constants.SecurityConstants;
import com.faster.entity.system.SysLog;
import com.faster.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 日志表 服务接口
 */
public interface ISysLogService extends IService<SysLog> {

	/**
	 * 保存日志
	 * @param sysLog 日志实体
	 * @param from 内部调用标志
	 * @return succes、false
	 */
	@PostMapping("/log")
	Result<Boolean> saveLog(@RequestBody SysLog sysLog, @RequestHeader(SecurityConstants.FROM) String from);

}
