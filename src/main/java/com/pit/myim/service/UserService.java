package com.pit.myim.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pit.myim.entity.UserEntity;

/**
 * @author deng
 * @email
 * @date 2019-09-16 17:23:54
 */
public interface UserService extends IService<UserEntity> {

    UserEntity getUserByUserName(String userName);


}

