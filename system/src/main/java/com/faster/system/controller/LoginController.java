package com.faster.system.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.faster.constants.CommonConstants;
import com.faster.entity.system.SysUser;
import com.faster.system.service.ISysUserService;
import com.faster.utils.JwtTokenUtils;
import com.faster.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户登陆模块
 */
@RestController
@RequestMapping("/user")
public class LoginController {

	@Autowired
	private ISysUserService sysUserService;

	/**
	 * 用户登陆
	 */
	@PostMapping("/login")
	public Result login(HttpServletRequest request, HttpServletResponse response, String username, String password) {
		Result<Map> result = new Result<>();
		// 根据用户名+密码获取用户信息
		SysUser user = sysUserService.getOne(Wrappers.<SysUser>lambdaQuery()
				.eq(SysUser::getUsername, username)
				.eq(SysUser::getPassword, password)
		);
		if(user == null) {
			result.setMsg("用户名或密码不正确！");
			result.setCode(CommonConstants.FAIL);
			return result;
		}

		// 校验成功，生成token  TODO 后续会将token存储在redis里
		String token = JwtTokenUtils.createToken(username);

		Map<String, Object> map = new HashMap<>(3);
		map.put("access_token", token);
		map.put("refresh_token", token);
		map.put("expires_in", JwtTokenUtils.TOKEN_VALIDITY_IN_MILLISECONDS);
		result.setData(map);
		return result;
	}

	/**
	 * 刷新token  TODO 这个方法有问题，临时应付一下。后续改造
	 */
	@PostMapping("/refreshToken")
	public Result refreshToken(HttpServletRequest request, HttpServletResponse response, String refresh_token) {
		Result<Map> result = new Result<>();
		// 校验成功，返回token
		String authorization = request.getHeader("Authorization");
		String token = JwtTokenUtils.createToken(authorization);

		Map<String, Object> map = new HashMap<>(3);
		map.put("access_token", token);
		map.put("refresh_token", token);
		map.put("expires_in", JwtTokenUtils.TOKEN_VALIDITY_IN_MILLISECONDS);
		result.setData(map);
		return result;
	}

	/**
	 * 退出并删除token
	 */
	@DeleteMapping("/logout")
	public Result<Boolean> logout(HttpServletResponse response) {
		// 清除浏览器token
		response.addHeader(JwtTokenUtils.AUTHORIZATION_HEADER, "Bearer ");
		return Result.ok();
	}

	/**
	 * 令牌管理调用
	 * @param token token
	 */
	/*@DeleteMapping("/{token}")
	public Result<Boolean> removeToken(@PathVariable("token") String token) {
		OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
		if (accessToken == null || StrUtil.isBlank(accessToken.getValue())) {
			return R.ok();
		}

		OAuth2Authentication auth2Authentication = tokenStore.readAuthentication(accessToken);
		// 清空用户信息
		cacheManager.getCache(CacheConstants.USER_DETAILS).evict(auth2Authentication.getName());

		// 清空access token
		tokenStore.removeAccessToken(accessToken);

		// 清空 refresh token
		OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
		tokenStore.removeRefreshToken(refreshToken);
		return R.ok();
	}*/

}
