package com.pit.myim.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.MimeHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author deng
 * @date 2020/1/14 16:22
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 49)
@WebFilter(filterName = "AuthFilter", urlPatterns = "/*")
@Slf4j
public class AuthFilter implements Filter {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HeaderMapRequestWrapper wrapper = new HeaderMapRequestWrapper(request);
        try {
            String uid = request.getHeader("uid");

            if (StringUtils.isNotBlank(uid)) {
                String user_id = uid.substring(0, uid.indexOf("-"));
                String access_token = uid.substring(uid.indexOf(">") + 1);
                String access_token1 = redisTemplate.opsForValue().get("user_id:" + user_id);
                if (access_token.equals(access_token1)) {
                    String token = redisTemplate.opsForValue().get("user_token:" + access_token);
                    if (StrUtil.isNotEmpty(token)) {
                        Map<String, String> map = JSON.parseObject(token, Map.class);
                        String jwt_token = map.get("jwt_token");
                        String key = "Authorization";
                        String var = "Bearer " + jwt_token;
                        wrapper.addHeader(key, var);
                        filterChain.doFilter(wrapper, servletResponse);
                        return;
                    }
                }
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {

    }

    private void reflectSetparam(HttpServletRequest request, String key, String value) {
        Class<? extends HttpServletRequest> requestClass = request.getClass();
        try {
            Field field = requestClass.getDeclaredField("request");
            field.setAccessible(true);
            Object o = field.get(request);
            Field coyoteRequest = o.getClass().getDeclaredField("coyoteRequest");
            coyoteRequest.setAccessible(true);
            Object o1 = coyoteRequest.get(o);
            Field headers = o1.getClass().getDeclaredField("headers");
            headers.setAccessible(true);
            MimeHeaders o2 = (MimeHeaders) headers.get(o1);
            o2.addValue(key).setString(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
