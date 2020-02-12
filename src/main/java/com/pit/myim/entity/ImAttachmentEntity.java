package com.pit.myim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 附件表
 * 
 * @author deng
 * @email ${email}
 * @date 2020-01-16 11:29:00
 */
@Data
@TableName("im_attachment")
@Accessors(chain = true)
public class ImAttachmentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 附件id
	 */
	@TableId(type = IdType.UUID)
	private String attachmentId;
	/**
	 * 所属者id
	 */
	private String ownerId;
	/**
	 * 附件名称
	 */
	private String name;
	/**
	 * 
	 */
	private Double size;
	/**
	 * 附件相对路径
	 */
	private String path;
	/**
	 * 附件pdf相对路径
	 */
	private String pdfPath;
	/**
	 * 
	 */
	private String mainCate;
	/**
	 * 
	 */
	private String subCate;
	/**
	 * 
	 */
	private String createUserId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 
	 */
	private String thumbnail;


}
