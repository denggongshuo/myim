package com.pit.myim.dao;

import com.pit.myim.entity.ImAttachmentEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 附件表
 * 
 * @author deng
 * @email ${email}
 * @date 2020-01-16 11:29:00
 */
@Mapper
@Repository
public interface ImAttachmentDao extends BaseMapper<ImAttachmentEntity> {
	
}
