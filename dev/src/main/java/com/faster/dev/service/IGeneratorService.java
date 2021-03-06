package com.faster.dev.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.faster.entity.dev.GenConfig;

import java.util.List;
import java.util.Map;

/**
 * 代码生成器接口
 */
public interface IGeneratorService {

	/**
	 * 生成代码
	 * @param tableNames 表名称
	 * @return
	 */
	byte[] generatorCode(GenConfig tableNames);

	/**
	 * 分页查询表
	 * @param page 分页信息
	 * @param tableName 表名
	 * @param name 数据源ID
	 * @return
	 */
	IPage<List<Map<String, Object>>> getPage(Page page, String tableName, String name);

}
