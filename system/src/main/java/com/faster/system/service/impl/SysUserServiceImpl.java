package com.faster.system.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.faster.constants.CacheConstants;
import com.faster.constants.CommonConstants;
import com.faster.dto.system.UserDTO;
import com.faster.dto.system.UserInfo;
import com.faster.entity.system.SysDept;
import com.faster.entity.system.SysRole;
import com.faster.entity.system.SysUser;
import com.faster.entity.system.SysUserRole;
import com.faster.system.mapper.SysUserMapper;
import com.faster.system.service.*;
import com.faster.utils.Result;
import com.faster.vo.system.MenuVO;
import com.faster.vo.system.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户相关接口服务
 *
 * @author YD
 * @date 2020/9/14
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    //private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private ISysMenuService sysMenuService;
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ISysUserRoleService sysUserRoleService;

    /**
     * 保存用户信息
     * @param userDto DTO 对象
     * @return success/fail
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveUser(UserDTO userDto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        sysUser.setDelFlag(CommonConstants.STATUS_NORMAL);
        sysUser.setPassword(DigestUtils.md5DigestAsHex(userDto.getPassword().trim().getBytes()));
        baseMapper.insert(sysUser);
        List<SysUserRole> userRoleList = userDto.getRole().stream().map(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(sysUser.getUserId());
            userRole.setRoleId(roleId);
            return userRole;
        }).collect(Collectors.toList());
        return sysUserRoleService.saveBatch(userRoleList);
    }

    /**
     * 通过查用户的全部信息
     * @param sysUser 用户
     * @return
     */
    @Override
    public UserInfo getUserInfo(SysUser sysUser) {
        UserInfo userInfo = new UserInfo();
        userInfo.setSysUser(sysUser);
        // 设置角色列表 （ID）
        List<Integer> roleIds = sysRoleService.findRolesByUserId(sysUser.getUserId()).stream().map(SysRole::getRoleId)
                .collect(Collectors.toList());
        userInfo.setRoles(ArrayUtil.toArray(roleIds, Integer.class));

        // 设置权限列表（menu.permission）
        Set<String> permissions = new HashSet<>();
        roleIds.forEach(roleId -> {
            List<String> permissionList = sysMenuService.findMenuByRoleId(roleId).stream()
                    .filter(menuVo -> StringUtils.isNotEmpty(menuVo.getPermission())).map(MenuVO::getPermission)
                    .collect(Collectors.toList());
            permissions.addAll(permissionList);
        });
        userInfo.setPermissions(ArrayUtil.toArray(permissions, String.class));
        return userInfo;
    }

    /**
     * 分页查询用户信息（含有角色信息）
     * @param page 分页对象
     * @param userDTO 参数列表
     * @return
     */
    @Override
    public IPage getUserWithRolePage(Page page, UserDTO userDTO) {
        return baseMapper.getUserVosPage(page, userDTO);
    }

    /**
     * 通过ID查询用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    @Override
    public UserVO getUserVoById(Integer id) {
        return baseMapper.getUserVoById(id);
    }

    /**
     * 删除用户
     * @param sysUser 用户
     * @return Boolean
     */
    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#sysUser.username")
    public Boolean removeUserById(SysUser sysUser) {
        sysUserRoleService.removeRoleByUserId(sysUser.getUserId());
        this.removeById(sysUser.getUserId());
        return Boolean.TRUE;
    }

    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.username")
    public Result updateUserInfo(UserDTO userDto) {
        UserVO userVO = baseMapper.getUserVoByUsername(userDto.getUsername());

        String oldPassword = DigestUtils.md5DigestAsHex(userDto.getPassword().trim().getBytes());
        if (!oldPassword.equals(userVO.getPassword())) {
           return Result.failed("原密码错误，修改失败");
        }

        SysUser sysUser = new SysUser();
        sysUser.setPassword(DigestUtils.md5DigestAsHex(userDto.getNewpassword1().trim().getBytes()));
        sysUser.setPhone(userDto.getPhone());
        sysUser.setUserId(userVO.getUserId());
        sysUser.setAvatar(userDto.getAvatar());
        return Result.ok(this.updateById(sysUser));
    }

    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.username")
    public Boolean updateUser(UserDTO userDto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        sysUser.setUpdateTime(LocalDateTime.now());

        if (StrUtil.isNotBlank(userDto.getPassword())) {
            sysUser.setPassword(DigestUtils.md5DigestAsHex(userDto.getPassword().trim().getBytes()));
        }
        this.updateById(sysUser);

        sysUserRoleService.remove(Wrappers.<SysUserRole>update().lambda().eq(SysUserRole::getUserId, userDto.getUserId()));
        userDto.getRole().forEach(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(sysUser.getUserId());
            userRole.setRoleId(roleId);
            userRole.insert();
        });
        return Boolean.TRUE;
    }

    /**
     * 查询上级部门的用户信息
     * @param username 用户名
     * @return R
     */
    @Override
    public List<SysUser> listAncestorUsersByUsername(String username) {
        SysUser sysUser = this.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));

        SysDept sysDept = sysDeptService.getById(sysUser.getDeptId());
        if (sysDept == null) {
            return null;
        }

        Integer parentId = sysDept.getParentId();
        return this.list(Wrappers.<SysUser>query().lambda().eq(SysUser::getDeptId, parentId));
    }

}
