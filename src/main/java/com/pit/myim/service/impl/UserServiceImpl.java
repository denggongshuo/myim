package com.pit.myim.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pit.myim.dao.UserDao;
import com.pit.myim.entity.PageFrom;
import com.pit.myim.entity.UserEntity;
import com.pit.myim.service.UserService;
import com.pit.myim.util.PageUtils;
import com.pit.myim.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("userService")

@Slf4j
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {


    @Override
    public PageUtils getPage(PageFrom pageFrom, String keyword) {
        Map<String, Object> map = BeanUtil.beanToMap(pageFrom);
        List<String> defaultOrderFields = pageFrom.getDefaultOrderFields();
        String[] strArray = Convert.toStrArray(defaultOrderFields);
        Boolean asc = pageFrom.getAsc();
        IPage<UserEntity> page = page(
                new Page<UserEntity>(pageFrom.getPageNum(),pageFrom.getPageSize()),
                new QueryWrapper<UserEntity>()
                        .select("user_id","username","name","sex","mobile","userphoto_url")
                        .like(StringUtils.isNotBlank(keyword), "name", keyword)
                        .or()
                        .like(StringUtils.isNotBlank(keyword), "username", keyword)
                        .eq("is_del",false)
                        .eq("is_enable",true)
                        .orderByAsc(asc,strArray)
                        .orderByDesc(!asc, strArray)
        );
        return new PageUtils(page);

    }

    @Override
    public UserEntity getUserByUserName(String userName) {
        UserEntity userEntity = getOne(new QueryWrapper<UserEntity>()
                .eq("username", userName));
        return userEntity;
    }
}



