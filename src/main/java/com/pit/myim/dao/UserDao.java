package com.pit.myim.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pit.myim.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author deng
 * @email
 * @date 2019-09-16 17:23:54
 */
@Mapper
@Repository
public interface UserDao extends BaseMapper<UserEntity> {


}
