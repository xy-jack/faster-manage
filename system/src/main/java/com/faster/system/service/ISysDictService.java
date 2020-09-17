package com.faster.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.faster.entity.system.SysDict;

/**
 * 字典表
 */
public interface ISysDictService extends IService<SysDict> {

	/**
	 * 根据ID 删除字典
	 * @param id
	 * @return
	 */
	void removeDict(Integer id);

	/**
	 * 更新字典
	 * @param sysDict 字典
	 * @return
	 */
	void updateDict(SysDict sysDict);

}
