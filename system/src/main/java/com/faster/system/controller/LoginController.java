package com.faster.system.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.faster.entity.system.SysUser;
import com.faster.system.service.ISysUserService;
import com.faster.system.utils.JwtTokenUtils;
import com.faster.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户登陆模块
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class LoginController {

	private static final String PASSWORD = "password";

	private static final String KEY_ALGORITHM = "AES";

	@Value("${security.encode.key:1234567812345678}")
	private String encodeKey;

	@Autowired
	private ISysUserService sysUserService;

	/**
	 * 用户登陆
	 */
	@PostMapping("/login")
	public Result login(HttpServletRequest request, HttpServletResponse response, String username, String password) {
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			return Result.failed("用户名或密码不能为空！");
		}

		// 根据用户名获取用户信息
		SysUser user = sysUserService.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username));
		if(user == null) {
			return Result.failed("用户名不存在！");
		}

		// 先用AES算法解密后在用md5加密跟数据库进行比较
		password = DigestUtils.md5DigestAsHex(decryptAES(password, encodeKey).trim().getBytes());
		// 对比是否相等
		if(!password.equals(user.getPassword())) {
			return Result.failed("用户名或密码不正确！");
		}

		// 通过用户名和密码创建一个 Authentication 认证对象，实现类为 UsernamePasswordAuthenticationToken
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

		// 校验成功，生成token  TODO 后续会将token存储在redis里
		String token = JwtTokenUtils.createToken(authenticationToken);

		Authentication authentication = JwtTokenUtils.getAuthentication(token);
		// 将 Authentication 绑定到 SecurityContext
		SecurityContextHolder.getContext().setAuthentication(authentication);

		Map<String, Object> map = new HashMap<>(3);
		map.put("access_token", token);
		map.put("refresh_token", token);
		map.put("expires_in", JwtTokenUtils.TOKEN_VALIDITY_IN_MILLISECONDS);
		return Result.ok(map) ;
	}

	/**
	 * 刷新token  TODO 这个方法有问题，临时应付一下。后续改造
	 */
	@PostMapping("/refreshToken")
	public Result refreshToken(HttpServletRequest request, HttpServletResponse response, String refresh_token) {
		Result<Map> result = new Result<>();
		// 校验成功，返回token
		String authorization = request.getHeader("Authorization");
		//String token = JwtTokenUtils.createToken(authorization);
		String token = "234234g";

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
	 * 解密
	 */
	private static String decryptAES(String data, String pass) {
		AES aes = new AES(Mode.CBC, Padding.NoPadding, new SecretKeySpec(pass.getBytes(), KEY_ALGORITHM), new IvParameterSpec(pass.getBytes()));
		byte[] result = aes.decrypt(Base64.decode(data.getBytes(StandardCharsets.UTF_8)));
		return new String(result, StandardCharsets.UTF_8);
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
