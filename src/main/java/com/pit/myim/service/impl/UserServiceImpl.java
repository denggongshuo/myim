package com.pit.myim.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pit.myim.dao.UserDao;
import com.pit.myim.entity.UserEntity;
import com.pit.myim.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



@Service("userService")

@Slf4j
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Override
    public UserEntity getUserByUserName(String userName) {
        UserEntity userEntity = getOne(new QueryWrapper<UserEntity>()
                .eq("username", userName));
        return userEntity;
    }
}



