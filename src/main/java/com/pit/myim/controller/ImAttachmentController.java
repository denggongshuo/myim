package com.pit.myim.controller;

import com.pit.myim.entity.FileDto;
import com.pit.myim.entity.RES;
import com.pit.myim.util.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pit.myim.service.ImAttachmentService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 附件表
 *
 * @author deng
 * @email ${email}
 * @date 2020-01-16 11:29:00
 */
@RestController
@RequestMapping("/imattachment")
public class ImAttachmentController extends BaseController {

    @Autowired
    private ImAttachmentService imAttachmentService;

    @PostMapping("/uploadFile")
    public RES upload(MultipartFile files,FileDto dto) {
        dto.setUserId(getUserId());
        String path = null;
        Map<String,String> map = new HashMap<>();
        try {
            path = imAttachmentService.uploadFile(files, dto);
            map.put("src",path);
            map.put("name",files.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
            return RES.fail("上传失败");
        }
        return RES.ok(0,map);
    }


}
