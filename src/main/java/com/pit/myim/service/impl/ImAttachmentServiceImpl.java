package com.pit.myim.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pit.myim.dao.ImAttachmentDao;
import com.pit.myim.entity.FileDto;
import com.pit.myim.exception.ExceptionCast;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.pit.myim.entity.ImAttachmentEntity;
import com.pit.myim.service.ImAttachmentService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Date;


@Service("imAttachmentService")
public class ImAttachmentServiceImpl extends ServiceImpl<ImAttachmentDao, ImAttachmentEntity> implements ImAttachmentService {

    @Value("${root_http_img_path}")
    String root_http_img_path; //  => /img/
    @Value("${root_file_path}")
    String root_file_path;  //  => E:/work
    @Value("${server.servlet.context-path}")
    String contextPath; //   => //myim
    @Value("${server.port}")
    String port;

    @Override
    @Transactional
    public String uploadFile(MultipartFile file, FileDto dto) throws IOException {
        if (file == null) {
            ExceptionCast.cast(-1, "文件为空");
        }
        String filename = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        String filePath = genFilePath(inputStream, filename);
        String toPath = root_file_path + root_http_img_path + filePath;
        File tofile = new File(toPath);
        String ip = InetAddress.getLocalHost().getHostAddress();
        if (tofile.exists() && DigestUtil.md5Hex(tofile).equals(DigestUtil.md5Hex(file.getInputStream()))) {
            return /*"http://"+ip+":"+port+*/contextPath + root_http_img_path + filePath;
        }
        if (!tofile.exists()) {
            //先得到文件的上级目录，并创建上级目录，在创建文件
            tofile.getParentFile().mkdirs();
            try {
                //创建文件
                tofile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        file.transferTo(tofile);
        //保存信息
        ImAttachmentEntity attachmentEntity = new ImAttachmentEntity();
        attachmentEntity.setOwnerId(dto.getUserId())
                .setName(filename)
                .setSize(Convert.toDouble(file.getSize()))
                .setPath(contextPath + root_http_img_path + filePath)
                .setMainCate("im_hismsg")
                .setCreateUserId(dto.getUserId())
                .setCreateTime(new Date());
        save(attachmentEntity);
        return /*"http://"+ip+":"+port+*/contextPath + root_http_img_path + filePath;
    }

    String genFilePath(InputStream resource, String fileName) throws IOException {
        String md5Hex = DigestUtil.md5Hex(resource);
        String first = md5Hex.substring(0, 1);
        String seconed = md5Hex.substring(1, 2);
        String path = first + "/" + seconed + "/" + md5Hex + "/" + fileName;
        return path;
    }


}