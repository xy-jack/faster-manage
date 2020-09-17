package com.faster.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.faster.dto.system.UserDTO;
import com.faster.entity.system.SysUser;
import com.faster.system.service.ISysUserService;
import com.faster.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户管理模块
 *
 */
@RestController
@RequestMapping("/admin/user")
public class UserController {

	@Autowired
	private ISysUserService userService;

	/**
	 * 获取当前用户全部信息
	 * @return 用户信息
	 */
	@GetMapping(value = { "/info" })
	public Result info() {
		//String username = SecurityUtils.getUser().getUsername();
		String username = "admin";
		SysUser user = userService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));
		if (user == null) {
			return Result.failed("获取当前用户信息失败");
		}
		return Result.ok(userService.getUserInfo(user));
	}

	/**
	 * 获取指定用户全部信息
	 * @return 用户信息
	 */
	//@Inner
	@GetMapping("/info/{username}")
	public Result info(@PathVariable String username) {
		SysUser user = userService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));
		if (user == null) {
			return Result.failed(String.format("用户信息为空 %s", username));
		}
		return Result.ok(userService.getUserInfo(user));
	}

	/**
	 * 根据用户名查询用户信息
	 * @param username 用户名
	 * @return
	 */
	@GetMapping("/details/{username}")
	public Result user(@PathVariable String username) {
		SysUser condition = new SysUser();
		condition.setUsername(username);
		return Result.ok(userService.getOne(new QueryWrapper<>(condition)));
	}

	/**
	 * 通过ID查询用户信息
	 * @param id ID
	 * @return 用户信息
	 */
	@GetMapping("/{id}")
	public Result user(@PathVariable Integer id) {
		return Result.ok(userService.getUserVoById(id));
	}

	/**
	 * 删除用户信息
	 * @param id ID
	 * @return R
	 */
	@DeleteMapping("/{id}")
	public Result userDel(@PathVariable Integer id) {
		SysUser sysUser = userService.getById(id);
		return Result.ok(userService.removeUserById(sysUser));
	}

	/**
	 * 添加用户
	 * @param userDto 用户信息
	 * @return success/false
	 */
	@PostMapping
	public Result user(@RequestBody UserDTO userDto) {
		return Result.ok(userService.saveUser(userDto));
	}

	/**
	 * 更新用户信息
	 * @param userDto 用户信息
	 * @return R
	 */
	@PutMapping
	public Result updateUser(@Valid @RequestBody UserDTO userDto) {
		return Result.ok(userService.updateUser(userDto));
	}

	/**
	 * 分页查询用户
	 * @param page 参数集
	 * @param userDTO 查询参数列表
	 * @return 用户集合
	 */
	@GetMapping("/page")
	public Result getUserPage(Page page, UserDTO userDTO) {
		return Result.ok(userService.getUserWithRolePage(page, userDTO));
	}

	/**
	 * 修改个人信息
	 * @param userDto userDto
	 * @return success/false
	 */
	@PutMapping("/edit")
	public Result updateUserInfo(@Valid @RequestBody UserDTO userDto) {
		return userService.updateUserInfo(userDto);
	}

	/**
	 * 根据用户名称获取上级部门用户列表
	 *
	 * @param username 用户名称
	 * @return 上级部门用户列表
	 */
	@GetMapping("/ancestor/{username}")
	public Result listAncestorUsers(@PathVariable String username) {
		return Result.ok(userService.listAncestorUsersByUsername(username));
	}

}
