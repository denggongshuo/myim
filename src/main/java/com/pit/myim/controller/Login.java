package com.pit.myim.controller;

import cn.hutool.core.util.StrUtil;
import com.pit.myim.entity.RES;
import com.pit.myim.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author deng
 * @date 2020/1/13 14:56
 */
@RequestMapping
@RestController
public class Login {

    @Autowired
    UserService userService;

    @Autowired
    RestTemplate restTemplate;

    @Value("${loginUrl}")
    String loginUrl;

    @Value("${server.port}")
    String port;

    @Value("${server.servlet.context-path}")
    String context_path;

    @PostConstruct
    void ini() {
        context_path = context_path.equals("/") ? "" : context_path;
    }

    @PostMapping("/login")
    public Object login(@RequestBody Map map) {
        String loginName = map.get("loginName") + "";
        String password = map.get("password") + "";
        if (StringUtils.isBlank(loginName) || "null".equalsIgnoreCase(loginName))
            return RES.fail("账号不能为空");
        if (StringUtils.isBlank(password) || "null".equalsIgnoreCase(password))
            return RES.fail("密码不能为空");

        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("username", loginName);
        requestBody.put("password", password);
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        requestBody.put("ucenterUrl", StrUtil.format("http://{}:{}/{}/user/getUserByUserName",ip,port,context_path));
        try {
            Map<String, Object> res = restTemplate.postForObject(loginUrl, requestBody, Map.class);
            return res;
        } catch (RestClientException e) {
            e.printStackTrace();
            return RES.fail("登录失败");
        }
/*
        UserEntity user = userService.getUserByUserName(loginName);
        if (null == user)
            return RES.fail("账号不存在");

        if (!BCryptUtil.matches(password,user.getPassword()))
            return RES.fail("密码错误");
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", user.getUserId());
        result.put("name", user.getName());*/
    }


}
