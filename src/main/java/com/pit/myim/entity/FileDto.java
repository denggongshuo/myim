package com.pit.myim.entity;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author: deng
 * @Date: 2019/9/25 10:13
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class FileDto implements Serializable {

    private static final long serialVersionUID = 5256463308743869177L;

    @ApiModelProperty("所属模块")
    private String mainCate;

    @ApiModelProperty("文件类型")
    private String subCate;

    @ApiParam(value = "用户id")
    private String userId;

    //文件路径
    @ApiParam(value = "文件路径")
    private String filePath;

    //pdf路径
    @ApiParam(value = "pdf路径")
    private String pdfFilePath;


}
