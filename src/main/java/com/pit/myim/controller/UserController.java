package com.pit.myim.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pit.myim.entity.PageFrom;
import com.pit.myim.entity.RES;
import com.pit.myim.entity.UserEntity;
import com.pit.myim.service.UserService;
import com.pit.myim.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author deng
 * @date 2020/1/14 14:12
 */
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUserByUserName")
    public RES getUserByUserName(@RequestParam("username") String username){
        UserEntity one = userService.getOne(new QueryWrapper<UserEntity>()
        .eq("username", username));
        return RES.ok(one);
    }

    /**
     * 分页
     * @param pageFrom
     * @param keyword 查询条件（账号，姓名）
     * @return
     */
    @GetMapping("/page")
    public RES page(PageFrom pageFrom,String keyword){
        PageUtils page = userService.getPage(pageFrom,keyword);
      return RES.ok(page);
    }

    /**
     * 详情
     * @param userId
     * @return
     */
    @GetMapping("/getInfo/{userId}")
    public RES getInfo(@PathVariable("userId") String userId){
        UserEntity userEntity = userService.getById(userId);
        return RES.ok(userEntity);
    }
}
