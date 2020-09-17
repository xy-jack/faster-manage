package com.faster.security;

import lombok.Getter;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 扩展用户信息
 */
public class FasterUser extends User {

	/**
	 * 用户ID
	 */
	@Getter
	private Integer id;

	/**
	 * 部门ID
	 */
	@Getter
	private Integer deptId;

	public FasterUser(Integer id, Integer deptId, String username, String password, boolean enabled,
					  boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
					  Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.id = id;
		this.deptId = deptId;
	}

}
