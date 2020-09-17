package com.faster.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.faster.dto.system.MenuTree;
import com.faster.entity.system.SysMenu;
import com.faster.utils.Result;
import com.faster.vo.system.MenuVO;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author YD
 * @date  2020-09-12
 */
public interface ISysMenuService extends IService<SysMenu> {


    /**
     * 通过角色编号查询URL 权限
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<MenuVO> findMenuByRoleId(Integer roleId);

    /**
     * 级联删除菜单
     * @param id 菜单ID
     * @return true成功,false失败
     */
    Result removeMenuById(Integer id);

    /**
     * 更新菜单信息
     * @param sysMenu 菜单信息
     * @return 成功、失败
     */
    Boolean updateMenuById(SysMenu sysMenu);

    /**
     * 构建树
     * @param lazy 是否是懒加载
     * @param parentId 父节点ID
     * @return List
     */
    List<MenuTree> treeMenu(boolean lazy, Integer parentId);

    /**
     * 查询菜单
     * @param menuSet
     * @param parentId
     * @return List
     */
    List<MenuTree> filterMenu(Set<MenuVO> menuSet, Integer parentId);

}
