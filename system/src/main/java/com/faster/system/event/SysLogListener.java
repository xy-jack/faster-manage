package com.faster.system.event;

import com.faster.constants.SecurityConstants;
import com.faster.entity.system.SysLog;
import com.faster.system.service.ISysLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * 异步监听日志事件
 */
@Slf4j
@RequiredArgsConstructor
public class SysLogListener {

	private final ISysLogService sysLogService;

	@Async
	@Order
	@EventListener(SysLogEvent.class)
	public void saveSysLog(SysLogEvent event) {
		SysLog sysLog = (SysLog) event.getSource();
		sysLogService.saveLog(sysLog, SecurityConstants.FROM_IN);
	}

}
