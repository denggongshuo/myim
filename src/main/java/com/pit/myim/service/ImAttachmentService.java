package com.pit.myim.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pit.myim.entity.FileDto;
import com.pit.myim.entity.ImAttachmentEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * 附件表
 *
 * @author deng
 * @email ${email}
 * @date 2020-01-16 11:29:00
 */
public interface ImAttachmentService extends IService<ImAttachmentEntity> {


    String uploadFile(MultipartFile file, FileDto dto) throws IOException;
}

