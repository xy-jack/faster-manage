package com.faster.entity.dev;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 生成记录
 */
@Data
@TableName("gen_form_conf")
@EqualsAndHashCode(callSuper = true)
public class GenFormConf extends Model<GenFormConf> {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
	private Integer id;

	/**
	 * 表名称
	 */
	private String tableName;

	/**
	 * 表单信息
	 */
	private String formInfo;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	private LocalDateTime updateTime;

	/**
	 * 删除标记
	 */
	private String delFlag;

}
