package com.faster.vo.system;

import lombok.Data;

/**
 * 前端角色展示对象
 */
@Data
public class RoleVo {

	/**
	 * 角色id
	 */
	private Integer roleId;

	/**
	 * 菜单列表
	 */
	private String menuIds;

}
