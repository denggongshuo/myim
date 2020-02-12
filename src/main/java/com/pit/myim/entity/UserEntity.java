package com.pit.myim.entity;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author deng
 * @email
 * @date 2019-09-16 17:23:54
 */
@Data
@TableName("itsm_sys_user")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(type = IdType.UUID)
    private String userId;
    /**
     *
     */
    @ApiModelProperty("部门")
    private String departmentId;
    /**
     *
     */
    @ApiModelProperty("账号")
    private String username;
    /**
     *
     */
    @ApiModelProperty("用户名")
    private String name;
    /**
     *
     */
    @ApiModelProperty("密码")
    private String password;
    /**
     *
     */
    @ApiModelProperty("性别")
    private String sex;

    public String getSex() {
        if (StrUtil.isBlank(this.sex))
            return null;
        switch (this.sex) {
            case "0001":
                return "男";
            case "0002":
                return "女";
            case "0003":
                return "未知";
            default:
                return "未知";
        }

    }

    /**
     *
     */
    @ApiModelProperty("手机号")
    private String mobile;
    /**
     *
     */
    @ApiModelProperty("电话号码")
    private String tel;
    /**
     *
     */
    @ApiModelProperty("用户头像路径")
    private String userphotoUrl;
    /**
     *
     */
    @ApiModelProperty("邮箱")
    private String email;
    /**
     *
     */
    @ApiModelProperty("职务")
    private String post;

    @ApiModelProperty("军衔")
    private String militaryRank;

    /**
     *
     */
    @ApiModelProperty("身份证")
    private String cardId;
    /**
     *
     */
    @ApiModelProperty("排序id")
    private Integer orderId;
    /**
     *
     */
    @ApiModelProperty("是否启用")
    private Boolean isEnable;
    /**
     *
     */
    @ApiModelProperty("备注")
    private String remark;
    /**
     *
     */
    @ApiModelProperty("工作状态")
    private String workStatus;
    /**
     *
     */
    private String spellName;

    @ApiModelProperty("创建者id")
    private String createId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("是否删除")
    private Boolean isDel;

    @ApiModelProperty(value = "拥有角色名称（多个逗号隔开）")
    @TableField(exist = false)
    private String roleNamesStr;


}
