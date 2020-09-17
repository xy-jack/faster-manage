package com.faster.system.controller;

import com.faster.entity.system.SysMenu;
import com.faster.system.service.ISysMenuService;
import com.faster.utils.Result;
import com.faster.vo.system.MenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 菜单管理模块
 */
@RestController
@RequestMapping("/admin/menu")
public class MenuController {

	@Autowired
	private ISysMenuService sysMenuService;

	/**
	 * 返回当前用户的树形菜单集合
	 * @param parentId 父节点ID
	 * @return 当前用户的树形菜单
	 */
	@GetMapping
	public Result getUserMenu(Integer parentId) {
		// 获取符合条件的菜单
		Set<MenuVO> all = new HashSet<>();
		//SecurityUtils.getRoles().forEach(roleId -> all.addAll(sysMenuService.findMenuByRoleId(roleId)));
		all.addAll(sysMenuService.findMenuByRoleId(1));
		return Result.ok(sysMenuService.filterMenu(all, parentId));
	}

	/**
	 * 返回树形菜单集合
	 * @param lazy 		是否是懒加载
	 * @param parentId  父节点ID
	 * @return 树形菜单
	 */

	@GetMapping(value = "/tree")
	public Result getTree(boolean lazy, Integer parentId) {
		return Result.ok(sysMenuService.treeMenu(lazy, parentId));
	}

	/**
	 * 返回角色的菜单集合
	 * @param roleId 角色ID
	 * @return 属性集合
	 */
	@GetMapping("/tree/{roleId}")
	public Result getRoleTree(@PathVariable Integer roleId) {
		return Result.ok(
				sysMenuService.findMenuByRoleId(roleId).stream().map(MenuVO::getMenuId).collect(Collectors.toList()));
	}

	/**
	 * 通过ID查询菜单的详细信息
	 * @param id 菜单ID
	 * @return 菜单详细信息
	 */
	@GetMapping("/{id}")
	public Result getById(@PathVariable Integer id) {
		return Result.ok(sysMenuService.getById(id));
	}

	/**
	 * 新增菜单
	 * @param sysMenu 菜单信息
	 * @return 含ID 菜单信息
	 */
	//@SysLog("新增菜单")
	//@PreAuthorize("@pms.hasPermission('sys_menu_add')")
	@PostMapping
	public Result save(@RequestBody SysMenu sysMenu) {
		sysMenuService.save(sysMenu);
		return Result.ok(sysMenu);
	}

	/**
	 * 删除菜单
	 * @param id 菜单ID
	 * @return success/false
	 */
	//@SysLog("删除菜单")
	//@PreAuthorize("@pms.hasPermission('sys_menu_del')")
	@DeleteMapping("/{id}")
	public Result removeById(@PathVariable Integer id) {
		return sysMenuService.removeMenuById(id);
	}

	/**
	 * 更新菜单
	 * @param sysMenu
	 * @return
	 */
	//@SysLog("更新菜单")
	//@PreAuthorize("@pms.hasPermission('sys_menu_edit')")
	@PutMapping
	public Result update(@RequestBody SysMenu sysMenu) {
		return Result.ok(sysMenuService.updateMenuById(sysMenu));
	}

}
