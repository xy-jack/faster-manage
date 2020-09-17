package com.faster.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.faster.entity.system.SysMenu;
import com.faster.vo.system.MenuVO;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author YD
 * @since 2020-08-17
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 通过角色编号查询菜单
     * @param roleId 角色ID
     * @return List
     */
    List<MenuVO> listMenusByRoleId(Integer roleId);

    /**
     * 通过角色ID查询权限
     * @param roleIds Ids
     * @return List
     */
    List<String> listPermissionsByRoleIds(String roleIds);
}
