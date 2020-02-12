//package com.pit.myim.Interceptor;
//
//import com.alibaba.fastjson.JSON;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.tomcat.util.http.MimeHeaders;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.annotation.Order;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.http.HttpMethod;
//import org.springframework.stereotype.Component;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.lang.reflect.Field;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author deng
// * @date 2020/1/14 15:55
// */
//@Slf4j
//@Component
//@Order(-1)
//public class AuthInterceptor extends HandlerInterceptorAdapter  {
//
//    @Autowired
//    StringRedisTemplate stringRedisTemplate;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//            throws Exception {
//        //如果是options请求直接放过
//        if(request.getMethod().matches(HttpMethod.OPTIONS.name())){
//            return true;
//        }
//        if(!(handler instanceof HandlerMethod)) {
//            return true;
//        }
//        log.info("request请求地址path[{}] uri[{}]", request.getServletPath(),request.getRequestURI());
//        Map<String,String> headerses = new HashMap<>();
//        headerses.put("content-type","application/json");
//        modifyHeaders(request,headerses);
//        return true;
//    }
//
//    /**
//     * 修改请求头信息
//     * @param headerses
//     * @param request
//     */
//    private void modifyHeaders(HttpServletRequest request,Map<String, String> headerses) {
//        if (headerses == null || headerses.isEmpty()) {
//            return;
//        }
//        String uid = request.getHeader("uid");
//        if (StringUtils.isEmpty(uid)) {
//            return;
//        }
//        String user_id = uid.substring(0, uid.indexOf("-"));
//        String access_token = uid.substring(uid.indexOf(">")+1);
//        String access_token1 = stringRedisTemplate.opsForValue().get("user_id:" + user_id);
//        if (!access_token.equals(access_token1))
//            return;
//        String token = stringRedisTemplate.opsForValue().get("user_token:" + access_token);
//        if (null == token)
//            return;
//        Map<String,String> map = JSON.parseObject(token, Map.class);
//        String jwt_token = map.get("jwt_token");
//        String key = "Authorization";
//        String var = "Bearer "+jwt_token;
//        reflectSetparam(request,key,var);
//    }
//
//    private void reflectSetparam(HttpServletRequest request, String key, String value) {
//        Class<? extends HttpServletRequest> requestClass = request.getClass();
//        try {
//            Field field = requestClass.getDeclaredField("request");
//            field.setAccessible(true);
//            Object o = field.get(request);
//            Field coyoteRequest = o.getClass().getDeclaredField("coyoteRequest");
//            coyoteRequest.setAccessible(true);
//            Object o1 = coyoteRequest.get(o);
//            Field headers = o1.getClass().getDeclaredField("headers");
//            headers.setAccessible(true);
//            MimeHeaders o2 = (MimeHeaders) headers.get(o1);
//            o2.addValue(key).setString(value);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
//                           ModelAndView modelAndView) throws Exception {
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
//            throws Exception {}
//}
