package com.faster.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.faster.dto.system.UserDTO;
import com.faster.entity.system.SysUser;
import com.faster.vo.system.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户mapper
 *
 * @author YD
 * @date 2020/9/14
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 通过用户名查询用户信息（含有角色信息）
     * @param username 用户名
     * @return userVo
     */
    UserVO getUserVoByUsername(String username);

    /**
     * 分页查询用户信息（含角色）
     * @param page 分页
     * @param userDTO 查询参数
     * @return list
     */
    IPage<List<UserVO>> getUserVosPage(Page page, @Param("query") UserDTO userDTO);

    /**
     * 通过ID查询用户信息
     * @param id 用户ID
     * @return userVo
     */
    UserVO getUserVoById(Integer id);
}
