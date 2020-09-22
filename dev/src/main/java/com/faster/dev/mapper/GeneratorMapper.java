package com.faster.dev.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 代码生成器
 *
 * @author lengleng
 * @date 2018-07-30
 */
public interface GeneratorMapper {

	/**
	 * 分页查询表格
	 * @param page
	 * @param tableName
	 * @return
	 */
	IPage<List<Map<String, Object>>> queryList(Page page, @Param("tableName") String tableName);

	/**
	 * 查询表信息
	 * @param tableName 表名称
	 * @param dsName 数据源名称
	 * @return
	 */
	//@DS("#last")
	Map<String, String> queryTable(@Param("tableName") String tableName, String dsName);

	/**
	 * 查询表列信息
	 * @param tableName 表名称
	 * @param dsName 数据源名称
	 * @return
	 */
	//@DS("#last")
	List<Map<String, String>> queryColumns(@Param("tableName") String tableName, String dsName);

}
