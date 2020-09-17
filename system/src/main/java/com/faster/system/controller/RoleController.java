package com.faster.system.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.faster.entity.system.SysRole;
import com.faster.system.service.ISysRoleMenuService;
import com.faster.system.service.ISysRoleService;
import com.faster.utils.Result;
import com.faster.vo.system.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 角色管理模块
 */
@RestController
@RequestMapping("/admin/role")
public class RoleController {

	@Autowired
	private ISysRoleService sysRoleService;

	@Autowired
	private ISysRoleMenuService sysRoleMenuService;

	/**
	 * 通过ID查询角色信息
	 * @param id ID
	 * @return 角色信息
	 */
	@GetMapping("/{id}")
	public Result getById(@PathVariable Integer id) {
		return Result.ok(sysRoleService.getById(id));
	}

	/**
	 * 添加角色
	 * @param sysRole 角色信息
	 * @return success、false
	 */
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_role_add')")
	public Result save(@Valid @RequestBody SysRole sysRole) {
		return Result.ok(sysRoleService.save(sysRole));
	}

	/**
	 * 修改角色
	 * @param sysRole 角色信息
	 * @return success/false
	 */
	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys_role_edit')")
	public Result update(@Valid @RequestBody SysRole sysRole) {
		return Result.ok(sysRoleService.updateById(sysRole));
	}

	/**
	 * 删除角色
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_role_del')")
	public Result removeById(@PathVariable Integer id) {
		return Result.ok(sysRoleService.removeRoleById(id));
	}

	/**
	 * 获取角色列表
	 * @return 角色列表
	 */
	@GetMapping("/list")
	public Result listRoles() {
		return Result.ok(sysRoleService.list(Wrappers.emptyWrapper()));
	}

	/**
	 * 分页查询角色信息
	 * @param page 分页对象
	 * @return 分页对象
	 */
	@GetMapping("/page")
	public Result getRolePage(Page page) {
		return Result.ok(sysRoleService.page(page, Wrappers.emptyWrapper()));
	}

	/**
	 * 更新角色菜单
	 * @param roleVo 角色对象
	 * @return success、false
	 */
	@PutMapping("/menu")
	@PreAuthorize("@pms.hasPermission('sys_role_perm')")
	public Result saveRoleMenus(@RequestBody RoleVo roleVo) {
		SysRole sysRole = sysRoleService.getById(roleVo.getRoleId());
		return Result.ok(sysRoleMenuService.saveRoleMenus(sysRole.getRoleCode(), roleVo.getRoleId(), roleVo.getMenuIds()));
	}

}
