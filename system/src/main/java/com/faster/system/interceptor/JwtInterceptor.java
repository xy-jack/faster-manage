package com.faster.system.interceptor;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.faster.utils.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * token验证拦截器
 *
 * @author YD
 */
@Slf4j
public class JwtInterceptor extends HandlerInterceptorAdapter {

    // 登陆接口
    private static final String LOGIN_PATH = "/user/login";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String URI = request.getRequestURI();
        // 如果是登陆直接放过
        if(LOGIN_PATH.equals(URI)) {
            return true;
        }

        if (HttpMethod.OPTIONS.name().equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        // 获取请求头信息authorization信息
        String authorization = request.getHeader("Authorization");
        log.info("## authorization= {}", authorization);
        if (StringUtils.isBlank(authorization)) {
            log.info("### 用户未登录，请先登录 ###");
            return false;
        }

        // 获取token
        String token = authorization.substring(7);
        // 验证token是否有效--无效已做异常抛出，由全局异常处理后返回对应信息
        if(!JwtTokenUtils.validateToken(token)) {
            return false;
        };
        return true;
    }
}
