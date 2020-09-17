package com.faster.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.faster.entity.system.SysDictItem;
import com.faster.utils.Result;

/**
 * 字典项
 */
public interface ISysDictItemService extends IService<SysDictItem> {

	/**
	 * 删除字典项
	 * @param id 字典项ID
	 * @return
	 */
	Result removeDictItem(Integer id);

	/**
	 * 更新字典项
	 * @param item 字典项
	 * @return
	 */
	Result updateDictItem(SysDictItem item);

}
