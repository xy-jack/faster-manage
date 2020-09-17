package com.faster.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.faster.constants.CacheConstants;
import com.faster.entity.system.SysDict;
import com.faster.entity.system.SysDictItem;
import com.faster.enums.DictTypeEnum;
import com.faster.system.mapper.SysDictItemMapper;
import com.faster.system.service.ISysDictItemService;
import com.faster.system.service.ISysDictService;
import com.faster.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 字典项
 *
 * @author lengleng
 * @date 2019/03/19
 */
@Service
@RequiredArgsConstructor
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements ISysDictItemService {

	private final ISysDictService dictService;

	/**
	 * 删除字典项
	 * @param id 字典项ID
	 * @return
	 */
	@Override
	@CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
	public Result removeDictItem(Integer id) {
		// 根据ID查询字典ID
		SysDictItem dictItem = this.getById(id);
		SysDict dict = dictService.getById(dictItem.getDictId());
		// 系统内置
		Assert.state(!DictTypeEnum.SYSTEM.getType().equals(dict.getSystem()), "系统内置字典项目不能删除");
		return Result.ok(this.removeById(id));
	}

	/**
	 * 更新字典项
	 * @param item 字典项
	 * @return
	 */
	@Override
	@CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
	public Result updateDictItem(SysDictItem item) {
		// 查询字典
		SysDict dict = dictService.getById(item.getDictId());
		// 系统内置
		Assert.state(!DictTypeEnum.SYSTEM.getType().equals(dict.getSystem()), "系统内置字典项目不能修改");
		return Result.ok(this.updateById(item));
	}

}
