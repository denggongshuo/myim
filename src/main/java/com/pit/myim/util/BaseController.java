package com.pit.myim.util;


import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class BaseController {

    private final static String JWT_KOKRN = "OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE";

    public ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }


    public HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    public HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    public String getFromHeader(String key) {
        return getRequest().getHeader(key);
    }


    public String getJwtToken() {
        String attribute = (String) getRequest().getAttribute(JWT_KOKRN);
        return attribute;
    }

    //解析jwtToken
    public Map<String, String> parseJwtToken() {
        String jwtToken = getJwtToken();
        if (null == jwtToken){
            return new HashMap<>();
        }
        Map<String, String> map = null;
        try {
            if (StringUtils.isNotEmpty(jwtToken)) {
                String[] arr = jwtToken.split("\\.");
                String str = Base64.decodeStr(arr[1],CharsetUtil.UTF_8);
                map = (Map<String, String>) JSON.parse(str);
            }
        } catch (Exception e) {
            log.error("jwt解析失败！");
            return new HashMap<>();
        }
        return map;
    }

    public String getUserId() {
        String id = parseJwtToken().get("id");
        if (StringUtils.isEmpty(id)){
            return "not find id";
        }
        return id;
    }

    public String getName() {
        String name = parseJwtToken().get("name");
        if (StringUtils.isEmpty(name)){
            return "not find name";
        }
        return name;
    }

    public String getUsername() {
        String username = parseJwtToken().get("user_name");
        if (StringUtils.isEmpty(username)){
            return "not find username";
        }
        return username;
    }

    public Date now() {
        return new Date();
    }

}
