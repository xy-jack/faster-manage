package com.faster.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.faster.entity.system.SysDept;
import com.faster.system.service.ISysDeptService;
import com.faster.utils.Result;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 * 部门管理模块
 */
@RestController
@RequestMapping("/admin/dept")
public class DeptController {

	@Autowired
	private ISysDeptService sysDeptService;

	/**
	 * 通过ID查询
	 * @param id ID
	 * @return SysDept
	 */
	@GetMapping("/{id}")
	public Result getById(@PathVariable Integer id) {
		return Result.ok(sysDeptService.getById(id));
	}

	/**
	 * 返回树形菜单集合
	 * @return 树形菜单
	 */
	@GetMapping(value = "/tree")
	public Result listDeptTrees() {
		return Result.ok(sysDeptService.listDeptTrees());
	}

	/**
	 * 返回当前用户树形菜单集合
	 * @return 树形菜单
	 */
	@GetMapping(value = "/user-tree")
	public Result listCurrentUserDeptTrees() {
		return Result.ok(sysDeptService.listCurrentUserDeptTrees());
	}

	/**
	 * 添加
	 * @param sysDept 实体
	 * @return success/false
	 */
	@PostMapping
	public Result save(@Valid @RequestBody SysDept sysDept) {
		return Result.ok(sysDeptService.saveDept(sysDept));
	}

	/**
	 * 删除
	 * @param id ID
	 * @return success/false
	 */
	@DeleteMapping("/{id}")
	public Result removeById(@PathVariable Integer id) {
		return Result.ok(sysDeptService.removeDeptById(id));
	}

	/**
	 * 编辑
	 * @param sysDept 实体
	 * @return success/false
	 */
	@PutMapping
	public Result update(@Valid @RequestBody SysDept sysDept) {
		sysDept.setUpdateTime(LocalDateTime.now());
		return Result.ok(sysDeptService.updateDeptById(sysDept));
	}

	/**
	 * 根据部门名查询部门信息
	 * @param deptname 部门名
	 * @return
	 */
	@GetMapping("/details/{deptname}")
	public Result user(@PathVariable String deptname) {
		SysDept condition = new SysDept();
		condition.setName(deptname);
		return Result.ok(sysDeptService.getOne(new QueryWrapper<>(condition)));
	}

}
