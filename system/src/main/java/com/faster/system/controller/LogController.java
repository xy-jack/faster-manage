package com.faster.system.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.faster.entity.system.SysLog;
import com.faster.system.service.ISysLogService;
import com.faster.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 日志表 前端控制器
 */
@RestController
@RequestMapping("/admin/log")
public class LogController {

	@Autowired
	private ISysLogService sysLogService;

	/**
	 * 简单分页查询
	 * @param page 分页对象
	 * @param sysLog 系统日志
	 * @return
	 */
	@GetMapping("/page")
	public Result getLogPage(Page page, SysLog sysLog) {
		return Result.ok(sysLogService.page(page, Wrappers.query(sysLog)));
	}

	/**
	 * 删除日志
	 * @param id ID
	 * @return success/false
	 */
	@DeleteMapping("/{id}")
	public Result removeById(@PathVariable Long id) {
		return Result.ok(sysLogService.removeById(id));
	}

	/**
	 * 插入日志
	 * @param sysLog 日志实体
	 * @return success/false
	 */
	@PostMapping
	public Result save(@Valid @RequestBody SysLog sysLog) {
		return Result.ok(sysLogService.save(sysLog));
	}

}
